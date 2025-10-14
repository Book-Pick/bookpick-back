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
    // 1. 회원가입
   @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignRes>> signUp(@Valid @RequestBody SignReq req) {


        return ResponseEntity.status(HttpStatus.CREATED)   // 201
                .body(ApiResponse.success(SuccessCode.REGISTER_SUCCESS,authService.signUp(req)));           // code, message, DTO 반환
    }

    // 2. 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthRes>> login(@Valid @RequestBody LoginReq req, HttpServletResponse res){

        AuthRes authRes = authService.login(req, res);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.LOGIN_SUCCESS, authRes))    ;      //data 에 DTO 주기
    }



}

// 제목
// 내용
//