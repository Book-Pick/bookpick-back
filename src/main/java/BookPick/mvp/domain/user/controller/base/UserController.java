package BookPick.mvp.domain.user.controller.base;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springboot.kakao_boot_camp.domain.user.dto.base.create.UserCreateReq;
import springboot.kakao_boot_camp.domain.user.dto.base.create.UserCreateRes;
import springboot.kakao_boot_camp.domain.user.dto.base.read.UserGetRes;
import springboot.kakao_boot_camp.domain.user.dto.base.soft.UserSoftDeleteRes;
import springboot.kakao_boot_camp.domain.user.dto.base.update.UserUpdateReq;
import springboot.kakao_boot_camp.domain.user.dto.base.update.UserUpdateRes;
import springboot.kakao_boot_camp.domain.user.exception.NotHaveAdminRole;
import springboot.kakao_boot_camp.domain.user.service.base.UserService;
import springboot.kakao_boot_camp.domain.user.util.AdminManager;
import springboot.kakao_boot_camp.global.api.ApiResponse;
import springboot.kakao_boot_camp.global.api.SuccessCode;
import springboot.kakao_boot_camp.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final AdminManager adminManager;

    // 유저 생성
    @PostMapping
    public ResponseEntity<ApiResponse<UserCreateRes>> createUser(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                                 @RequestBody @Valid UserCreateReq req) {
        if (adminManager.isAdmin(currentUser.getAuthorities())) {
            throw new NotHaveAdminRole();
        }

        UserCreateRes res = userService.CreateUser(req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.CREATE_USER_SUCCESS, res));
    }

    // 유저 조회
    @GetMapping
    public ResponseEntity<ApiResponse<UserGetRes>> getUseProfile(@AuthenticationPrincipal CustomUserDetails currentUser) {
        UserGetRes res = userService.userProfileGet(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.GET_MY_PROFILE_SUCCESS, res));
    }


    // 유저 수정
    @PatchMapping
    public ResponseEntity<ApiResponse<UserUpdateRes>> updateUserProfile(@AuthenticationPrincipal CustomUserDetails currentUser
    , @RequestBody @Valid UserUpdateReq req) {
        UserUpdateRes res = userService.userProfileUpdate(currentUser.getId(), req);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.UPDATE_MY_PROFILE_SUCCESS, res));
    }


    // 유저 소프트 삭제
    public ResponseEntity<ApiResponse<UserSoftDeleteRes>> softDeleteUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        UserSoftDeleteRes res = userService.softDeleteProfile(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.GET_MY_PROFILE_SUCCESS, res));
    }


    // 유저 하드 삭제
    public ResponseEntity<ApiResponse<UserGetRes>> hardDeleteUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        if (adminManager.isAdmin(currentUser.getAuthorities())) {
            userService.hardDeleteUserProfile(currentUser.getId());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.GET_MY_PROFILE_SUCCESS, null));
    }
}
