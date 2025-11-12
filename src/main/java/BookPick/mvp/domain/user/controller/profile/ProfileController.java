package BookPick.mvp.domain.user.controller.profile;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springboot.kakao_boot_camp.domain.user.dto.base.read.UserGetRes;
import springboot.kakao_boot_camp.domain.user.dto.base.soft.UserSoftDeleteRes;
import springboot.kakao_boot_camp.domain.user.dto.base.update.UserUpdateReq;
import springboot.kakao_boot_camp.domain.user.dto.base.update.UserUpdateRes;
import springboot.kakao_boot_camp.domain.user.service.base.UserService;
import springboot.kakao_boot_camp.domain.user.util.AdminManager;
import springboot.kakao_boot_camp.global.api.ApiResponse;
import springboot.kakao_boot_camp.global.api.SuccessCode;
import springboot.kakao_boot_camp.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
public class ProfileController {

    private final UserService userService;
    private final AdminManager adminManager;


    // 1. 프로필 조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserGetRes>> getUseProfile(@AuthenticationPrincipal CustomUserDetails currentUser) {
        UserGetRes res = userService.userProfileGet(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.GET_MY_PROFILE_SUCCESS, res));
    }


    // 2. 프로필 수정
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserUpdateRes>> updateUserProfile(@AuthenticationPrincipal @Valid CustomUserDetails currentUser
            , @RequestBody @Valid UserUpdateReq req) {
        UserUpdateRes res = userService.userProfileUpdate(currentUser.getId(), req);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.UPDATE_MY_PROFILE_SUCCESS, res));
    }


    // 3. 회원 탈퇴 (소프트 삭제)
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<UserSoftDeleteRes>> softDeleteProfile(@AuthenticationPrincipal CustomUserDetails currentUser) {
        UserSoftDeleteRes res = userService.softDeleteProfile(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.SOFT_DELETE_MY_PROFILE_SUCCESS, res));
    }
}
