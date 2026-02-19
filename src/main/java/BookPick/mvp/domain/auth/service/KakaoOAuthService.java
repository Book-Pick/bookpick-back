package BookPick.mvp.domain.auth.service;

import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.auth.dto.KakaoTokenResponse;
import BookPick.mvp.domain.auth.dto.KakaoUserInfo;
import BookPick.mvp.domain.auth.dto.LoginRes;
import BookPick.mvp.domain.ReadingPreference.service.ReadingPreferenceService;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.global.util.JwtUtil;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class KakaoOAuthService {

    private static final String KAKAO_AUTH_URL = "https://kauth.kakao.com";
    private static final String KAKAO_API_URL = "https://kapi.kakao.com";
    private static final String PROVIDER = "kakao";
    private static final String OAUTH_PASSWORD_PREFIX = "oauth-kakao-";

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ReadingPreferenceService readingPreferenceService;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoOAuthService(
            UserRepository userRepository,
            JwtUtil jwtUtil,
            ReadingPreferenceService readingPreferenceService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.readingPreferenceService = readingPreferenceService;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${oauth.kakao.client-id:}")
    private String clientId;

    @Value("${oauth.kakao.client-secret:}")
    private String clientSecret;

    @Value("${oauth.kakao.redirect-uri:}")
    private String redirectUri;

    public String getKakaoAuthUrl() {
        return KAKAO_AUTH_URL
                + "/oauth/authorize"
                + "?client_id="
                + clientId
                + "&redirect_uri="
                + redirectUri
                + "&response_type=code";
    }

    @Transactional
    public LoginRes processKakaoCallback(String code) {
        // 1. 인가 코드로 카카오 액세스 토큰 요청
        KakaoTokenResponse tokenResponse = getKakaoAccessToken(code);

        // 2. 액세스 토큰으로 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = getKakaoUserInfo(tokenResponse.accessToken());

        // 3. 사용자 처리 (신규/기존) 및 JWT 발급
        return processUser(userInfo);
    }


    // 카카오한테 액세스 토큰 요청
    private KakaoTokenResponse getKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        if (clientSecret != null && !clientSecret.isBlank()) {
            params.add("client_secret", clientSecret);
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponse> response =
                restTemplate.postForEntity(
                        KAKAO_AUTH_URL + "/oauth/token", request, KakaoTokenResponse.class);

        return response.getBody();
    }

    private KakaoUserInfo getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfo> response =
                restTemplate.exchange(
                        KAKAO_API_URL + "/v2/user/me",
                        HttpMethod.GET,
                        request,
                        KakaoUserInfo.class);

        return response.getBody();
    }


    // DB에 사용자 저장 및 JWT 포함한 LoginRes반환
    private LoginRes processUser(KakaoUserInfo userInfo) {
        String providerId = String.valueOf(userInfo.id());

        // 기존 OAuth 사용자 조회
        User user =
                userRepository
                        .findByProviderAndProviderId(PROVIDER, providerId)
                        .orElseGet(
                                () -> {
                                    // 이메일로 기존 사용자 확인
                                    String email = userInfo.getEmail();
                                    if (email != null
                                            && userRepository.existsByEmail(email)) {
                                        // 기존 일반 사용자가 있으면 OAuth 정보 연동
                                        User existingUser =
                                                userRepository.findByEmail(email).orElseThrow();
                                        existingUser.setProvider(PROVIDER);
                                        existingUser.setProviderId(providerId);
                                        return userRepository.save(existingUser);
                                    }

                                    // 신규 사용자 생성
                                    User newUser =
                                            User.builder()
                                                    .email(email != null ? email : providerId + "@kakao.user")
                                                    .password(createOAuthPassword(providerId))
                                                    .nickname(userInfo.getNickname())
                                                    .profileImageUrl(userInfo.getProfileImageUrl())
                                                    .provider(PROVIDER)
                                                    .providerId(providerId)
                                                    .role(Roles.ROLE_USER)
                                                    .isFirstLogin(true)
                                                    .build();
                                    User savedUser = userRepository.save(newUser);
                                    readingPreferenceService.addClearReadingPreference(
                                            savedUser.getId());
                                    return savedUser;
                                });

        // JWT 토큰 생성
        String authorities = user.getRole().name();
        String accessToken = jwtUtil.createAccessTokenForOAuth(user, authorities);
        String refreshToken = jwtUtil.createRefreshTokenForOAuth(user);

        boolean isFirstLogin = user.isFirstLogin();
        if (isFirstLogin) {
            user.setFirstLogin(false);
        }

        return new LoginRes(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getBio(),
                user.getProfileImageUrl(),
                isFirstLogin,
                accessToken,
                refreshToken);
    }

    private String createOAuthPassword(String providerId) {
        return passwordEncoder.encode(OAUTH_PASSWORD_PREFIX + providerId + "-" + UUID.randomUUID());
    }
}
