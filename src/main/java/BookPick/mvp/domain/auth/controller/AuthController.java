package BookPick.mvp.domain.auth.controller;

import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.domain.auth.dto.AuthDtos.*;
import BookPick.mvp.domain.auth.service.AuthService;
import BookPick.mvp.global.api.SuccessCode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignRes>> signUp(@Valid @RequestBody SignReq req) {
        SignRes signRes = authService.signUp(req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.REGISTER_SUCCESS, signRes));
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginRes>> login(@Valid @RequestBody LoginReq req, HttpServletResponse res) {
        LoginRes loginRes = authService.login(req, res);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.LOGIN_SUCCESS, loginRes));
    }
}
