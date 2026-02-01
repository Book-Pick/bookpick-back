package BookPick.mvp.domain.user.controller.passWord;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.user.dto.passWord.PassWordChangeReq;
import BookPick.mvp.domain.user.enums.user.UserSuccessCode;
import BookPick.mvp.domain.user.service.passWord.PassWordService;
import BookPick.mvp.global.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/my/password")
@RequiredArgsConstructor
public class PasswordContorller {
    private final PassWordService passWordService;

    @PostMapping("/change")
    @Operation(
            summary = "비밀번호 변경",
            description = "현재 비밀번호를 확인하고 새 비밀번호로 변경합니다",
            tags = {"User"})
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "변경할 비밀번호와 확인 비밀번호가 일치하지 않습니다", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "현재 비밀번호가 올바르지 않습니다", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody @Valid PassWordChangeReq req) {
        passWordService.PassWordChange(currentUser.getId(), req);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(UserSuccessCode.PASSWORD_CHANGE_SUCCESS, null));
    }
}
