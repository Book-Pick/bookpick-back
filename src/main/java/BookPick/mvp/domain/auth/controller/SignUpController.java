package BookPick.mvp.domain.auth.controller;

import BookPick.mvp.domain.auth.dto.SignReq;
import BookPick.mvp.domain.auth.dto.SignRes;
import BookPick.mvp.domain.auth.service.SignUpService;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController // v1, v2 같은 버전은 추후 버전 관리를 위해 필요한 것인데 해당 프로젝트는 학습용 이므로 추후에 유지 보수 예정 X -> 따라서 버전 명 명시
// 안할 예정
@RequestMapping("/api/v1/auth/signup")
public class SignUpController {
    private final SignUpService authService;

    @PostMapping
    @Operation(
            summary = "회원가입",
            description = "회원가입",
            tags = {"Auth"})
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 존재하는 이메일입니다", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<SignRes>> signUp(
            @RequestBody @Valid SignReq req, HttpServletResponse servletRes) {
        SignRes res = authService.signUp(req); // data 얻기

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.REGISTER_SUCCESS, res));
    }
}
