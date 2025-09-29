package BookPick.mvp.domain.auth.controller;

import BookPick.mvp.global.ApiResponse;
import BookPick.mvp.domain.auth.dto.AuthDtos.*;
import BookPick.mvp.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    // 1. 회원가입
   @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignRes>> signUp(@Valid @RequestBody SignReq req) {
        SignRes res = authService.signUp(req);
        System.out.println(res);
        return ResponseEntity.status(HttpStatus.CREATED)   // 201
                .body(ApiResponse.created("create",res));           // created 사용
    }

    // 2. 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthRes>> login(@Valid @RequestBody LoginReq req, HttpServletResponse res){

        AuthRes authRes = authService.login(req, res);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("success", authRes))    ;      //data 에 DTO 주기
    }



}
