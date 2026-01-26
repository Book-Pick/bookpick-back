package BookPick.mvp.domain.auth.controller;

import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import BookPick.mvp.domain.auth.service.LogoutService;
import BookPick.mvp.domain.auth.service.CustomUserDetails;

@RestController
@RequestMapping("/api/v1/auth/logout")
@RequiredArgsConstructor
public class LogoutController {

    private final LogoutService logoutService;

    @PostMapping
    @Operation(summary = "로그아웃", description = "로그아웃", tags = {"Auth"})
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response,
                                                    @AuthenticationPrincipal CustomUserDetails currentUser) {

        logoutService.logout(currentUser, request, response);

        return ResponseEntity.ok(ApiResponse.success(SuccessCode.LOGOUT_SUCCESS, null));
    }
}
