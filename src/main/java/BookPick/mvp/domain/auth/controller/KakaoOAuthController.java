package BookPick.mvp.domain.auth.controller;

import BookPick.mvp.domain.auth.dto.LoginRes;
import BookPick.mvp.domain.auth.dto.OAuthTokenRequest;
import BookPick.mvp.domain.auth.exception.InvalidOAuthCodeException;
import BookPick.mvp.domain.auth.service.KakaoOAuthService;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.RefreshTokenCookieManager;
import BookPick.mvp.domain.auth.util.OAuthTempCodeStore;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth", description = "소셜 로그인 API")
@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final RefreshTokenCookieManager refreshTokenCookieManager;
    private final OAuthTempCodeStore tempCodeStore;

    @Value("${oauth.front-redirect-uri:http://localhost:5173/auth/kakao/callback}")
    private String frontRedirectUri;

    @GetMapping("/kakao")
    @Operation(summary = "카카오 로그인 URL 조회", description = "카카오 OAuth 인증 페이지로 리다이렉트할 URL을 반환합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "카카오 인증 URL 반환 성공")
    })
    public ResponseEntity<ApiResponse<Map<String, String>>> getKakaoAuthUrl() {
        String authUrl = kakaoOAuthService.getKakaoAuthUrl();
        return ResponseEntity.ok(
                ApiResponse.success(SuccessCode.LOGIN_SUCCESS, Map.of("authUrl", authUrl)));
    }

    @GetMapping("/kakao/callback")
    @Operation(
            summary = "카카오 로그인 콜백",
            description = "카카오 인증 후 임시 코드를 생성하고 프론트엔드로 리다이렉트합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "302",
                description = "프론트엔드로 리다이렉트"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "카카오 API 호출 실패")
    })
    public void kakaoCallback(
            @RequestParam("code") String code, HttpServletResponse servletResponse)
            throws IOException {

        // 카카오 인증 처리 및 로그인 정보 생성
        LoginRes loginRes = kakaoOAuthService.processKakaoCallback(code);

        // 임시 코드 생성 (30초 유효)
        String tempCode = tempCodeStore.store(loginRes);

        // 프론트엔드로 리다이렉트 -> 프론트 이슈 해결
        String redirectUrl = frontRedirectUri + "?code=" + tempCode;
        servletResponse.sendRedirect(redirectUrl);
    }

    @PostMapping("/token")
    @Operation(
            summary = "OAuth 토큰 교환",
            description = "임시 코드를 검증하고 실제 JWT 토큰을 발급합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "토큰 발급 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "유효하지 않은 임시 코드")
    })
    public ResponseEntity<ApiResponse<LoginRes>> exchangeToken(
            @Valid @RequestBody OAuthTokenRequest request, HttpServletResponse servletResponse) {

        // 임시 코드로 로그인 정보 조회 (일회용, 30초 내 사용)
        LoginRes loginRes =
                tempCodeStore.consume(request.code()).orElseThrow(InvalidOAuthCodeException::new);

        // 리프레시 토큰을 HttpOnly 쿠키에 설정
        refreshTokenCookieManager.addRefreshTokenCookie(servletResponse, loginRes.refreshToken());

        // 응답에서 리프레시 토큰 제거
        LoginRes result = LoginRes.fromWithoutRefreshToken(loginRes, loginRes.accessToken());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.KAKAO_LOGIN_SUCCESS, result));
    }
}
