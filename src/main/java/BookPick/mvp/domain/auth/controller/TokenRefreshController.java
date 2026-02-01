package BookPick.mvp.domain.auth.controller;

import BookPick.mvp.domain.auth.dto.LoginRes;
import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.auth.service.TokenRefreshService;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.RefreshTokenCookieManager;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/token")
@RequiredArgsConstructor
public class TokenRefreshController {

    private final RefreshTokenCookieManager refreshTokenCookieManager;
    private final TokenRefreshService tokenRefreshService;

    /** 🔄 Refresh Token을 이용해 Access Token 재발급 */
    @PostMapping("/refresh")
    @Operation(
            summary = "액세스 토큰 재발급",
            description = "액세스 토큰 재발급",
            tags = {"Auth"})
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "토큰이 만료되었거나 유효하지 않습니다", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<LoginRes>> refreshAccessToken(
            HttpServletRequest request,
            HttpServletResponse response,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        // 1️⃣ 쿠키에서 refresh token 추출
        String refreshToken = refreshTokenCookieManager.getRefreshTokenFromCookie(request);

        // 2️⃣ 새 access + refresh token 생성
        LoginRes newTokens = tokenRefreshService.refreshTokens(currentUser, refreshToken);

        // 3️⃣ 새 refresh token을 쿠키에 다시 설정
        refreshTokenCookieManager.addRefreshTokenCookie(response, newTokens.refreshToken());

        // 4️⃣ refresh token은 response에 포함하지 않음
        LoginRes result = LoginRes.fromWithoutRefreshToken(newTokens, newTokens.accessToken());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.TOKEN_REFERSH_SUCCESS, result));
    }
}
