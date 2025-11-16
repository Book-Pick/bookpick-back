package BookPick.mvp.domain.auth.controller;


import BookPick.mvp.domain.auth.dto.SignReq;
import BookPick.mvp.domain.auth.dto.SignRes;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import BookPick.mvp.domain.auth.service.SignUpService;

@RequiredArgsConstructor
@RestController     // v1, v2 같은 버전은 추후 버전 관리를 위해 필요한 것인데 해당 프로젝트는 학습용 이므로 추후에 유지 보수 예정 X -> 따라서 버전 명 명시 안할 예정
@RequestMapping("/api/v1/auth/signup")
public class SignUpController {
    private final SignUpService authService;

    @PostMapping
    public ResponseEntity<ApiResponse<SignRes>> signUp(@RequestBody @Valid SignReq req, HttpServletResponse servletRes) {
        SignRes res = authService.signUp(req);    //data 얻기

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.REGISTER_SUCCESS, res));
    }
}