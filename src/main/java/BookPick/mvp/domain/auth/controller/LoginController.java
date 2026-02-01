package BookPick.mvp.domain.auth.controller;

import BookPick.mvp.domain.auth.dto.LoginReq;
import BookPick.mvp.domain.auth.dto.LoginRes;
import BookPick.mvp.domain.auth.service.LoginService;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.RefreshTokenCookieManager;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth/login")
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final LoginService loginService;
    private final RefreshTokenCookieManager refreshTokenCookieManager;

    @PostMapping
    @Operation(
            summary = "로그인",
            description = "로그인",
            tags = {"Auth"})
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호가 잘못되었습니다", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<LoginRes>> login(
            @RequestBody @Valid LoginReq req,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {

        // 1. 액세스 토큰 포함 DTO 생성
        LoginRes res = loginService.login(req, servletRequest);

        // 2. 리프레시 토큰 포함
        refreshTokenCookieManager.addRefreshTokenCookie(servletResponse, res.refreshToken());

        // 3. Login response Dto에서 Refresh token 빼기
        LoginRes result = LoginRes.fromWithoutRefreshToken(res, res.accessToken());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.LOGIN_SUCCESS, result));
    }
}
