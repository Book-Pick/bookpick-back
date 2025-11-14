package BookPick.mvp.domain.user.controller.profile;


import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.user.dto.base.UserReq;
import BookPick.mvp.domain.user.dto.base.UserRes;
import BookPick.mvp.domain.user.dto.base.delete.UserSoftDeleteRes;
import BookPick.mvp.domain.user.enums.UserSuccessCode;
import BookPick.mvp.domain.user.service.base.UserService;
import BookPick.mvp.domain.user.util.AdminManager;
import BookPick.mvp.global.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
public class ProfileController {

    private final UserService userService;
    private final AdminManager adminManager;


    // 1. 프로필 조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserRes>> getUseProfile(@AuthenticationPrincipal CustomUserDetails currentUser) {
        UserRes res = userService.userProfileGet(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(UserSuccessCode.GET_MY_PROFILE_SUCCESS, res));
    }


    // 2. 프로필 수정
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserRes>> updateUserProfile(@AuthenticationPrincipal @Valid CustomUserDetails currentUser
            , @RequestBody @Valid UserReq req) {
        UserRes res = userService.userProfileUpdate(currentUser.getId(), req);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(UserSuccessCode.UPDATE_MY_PROFILE_SUCCESS, res));
    }


    // 3. 회원 탈퇴 (소프트 삭제)
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<UserSoftDeleteRes>> softDeleteProfile(@AuthenticationPrincipal CustomUserDetails currentUser) {
        UserSoftDeleteRes res = userService.softDeleteProfile(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(UserSuccessCode.SOFT_DELETE_MY_PROFILE_SUCCESS, res));
    }
}
