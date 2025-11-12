package BookPick.mvp.domain.user.controller.passWord;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.kakao_boot_camp.domain.user.dto.passWord.PassWordChangeReq;
import springboot.kakao_boot_camp.domain.user.service.passWord.PassWordService;
import springboot.kakao_boot_camp.global.api.ApiResponse;
import springboot.kakao_boot_camp.global.api.SuccessCode;
import springboot.kakao_boot_camp.security.CustomUserDetails;


@RestController
@RequestMapping("/api/v1/my/password")
@RequiredArgsConstructor
public class PasswordContorller {
    private final PassWordService passWordService;

    @PostMapping("/change")
    public ResponseEntity<ApiResponse<Void>> changePassword(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                            @RequestBody @Valid PassWordChangeReq req) {
        passWordService.PassWordChange(currentUser.getId(), req);


        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.PASSWORD_CHANGE_SUCCESS, null));
    }
}
