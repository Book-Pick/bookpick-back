package BookPick.mvp.domain.user.controller;

import BookPick.mvp.global.ApiResponse;
import BookPick.mvp.domain.user.dto.AuthDtos.*;
import BookPick.mvp.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiResponse<AuthRes>> login(@Valid @RequestBody LoginReq req){

        AuthRes authRes = authService.login(req);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("success", authRes))    ;      //data 에 DTO 주기
    }

    // 3. 로그아웃
    // Note : 서베에서는 별도 로직 x, 클라이언트가 토큰 지움
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("success", null));
    }


}
