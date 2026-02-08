package BookPick.mvp.domain.auth.controller;

import BookPick.mvp.domain.auth.dto.LoginRes;
import BookPick.mvp.domain.auth.service.KakaoOAuthService;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.RefreshTokenCookieManager;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth", description = "소셜 로그인 API")
@RestController
@RequestMapping("/api/v1/oauth/kakao")
@RequiredArgsConstructor
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final RefreshTokenCookieManager refreshTokenCookieManager;

    @GetMapping
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

    @GetMapping("/callback")
    @Operation(
            summary = "카카오 로그인 콜백",
            description = "카카오 인증 후 콜백을 처리하고 JWT 토큰을 발급합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "카카오 로그인 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 인가 코드"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "카카오 API 호출 실패")
    })
    public ResponseEntity<ApiResponse<LoginRes>> kakaoCallback(
            @RequestParam("code") String code, HttpServletResponse servletResponse) {

        LoginRes res = kakaoOAuthService.processKakaoCallback(code);

        // 리프레시 토큰을 쿠키에 설정
        refreshTokenCookieManager.addRefreshTokenCookie(servletResponse, res.refreshToken());

        // 응답에서 리프레시 토큰 제거
        LoginRes result = LoginRes.fromWithoutRefreshToken(res, res.accessToken());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.KAKAO_LOGIN_SUCCESS, result));
    }
}
