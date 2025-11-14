package BookPick.mvp.domain.user.controller.base;


import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.user.dto.base.UserReq;
import BookPick.mvp.domain.user.dto.base.UserRes;
import BookPick.mvp.domain.user.dto.base.delete.UserSoftDeleteRes;
import BookPick.mvp.domain.user.enums.UserSuccessCode;
import BookPick.mvp.domain.user.exception.NotHaveAdminRole;
import BookPick.mvp.domain.user.service.base.UserService;
import BookPick.mvp.domain.user.util.AdminManager;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final AdminManager adminManager;

    // 유저 생성
    @PostMapping
    public ResponseEntity<ApiResponse<UserRes>> createUser(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                           @RequestBody @Valid UserReq req) {
        if (adminManager.isAdmin(currentUser.getAuthorities())) {
            throw new NotHaveAdminRole();
        }

        UserRes res = userService.CreateUser(req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(UserSuccessCode.CREATE_USER_SUCCESS, res));
    }

    // 유저 조회
    @GetMapping
    public ResponseEntity<ApiResponse<UserRes>> getUseProfile(@AuthenticationPrincipal CustomUserDetails currentUser) {
        UserRes res = userService.userProfileGet(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(UserSuccessCode.GET_MY_PROFILE_SUCCESS, res));
    }


    // 유저 수정
    @PatchMapping
    public ResponseEntity<ApiResponse<UserRes>> updateUserProfile(@AuthenticationPrincipal CustomUserDetails currentUser
    , @RequestBody @Valid UserReq req) {
        UserRes res = userService.userProfileUpdate(currentUser.getId(), req);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(UserSuccessCode.UPDATE_MY_PROFILE_SUCCESS, res));
    }


    // 유저 소프트 삭제
    public ResponseEntity<ApiResponse<UserSoftDeleteRes>> softDeleteUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        UserSoftDeleteRes res = userService.softDeleteProfile(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(UserSuccessCode.GET_MY_PROFILE_SUCCESS, res));
    }


    // 유저 하드 삭제
    public ResponseEntity<ApiResponse<UserRes>> hardDeleteUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        if (adminManager.isAdmin(currentUser.getAuthorities())) {
            userService.hardDeleteUserProfile(currentUser.getId());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(UserSuccessCode.GET_MY_PROFILE_SUCCESS, null));
    }
}
