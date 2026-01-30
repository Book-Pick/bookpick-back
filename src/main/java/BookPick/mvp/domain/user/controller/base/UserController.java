// package BookPick.mvp.domain.user.controller.base;
//
//
// import BookPick.mvp.domain.auth.service.CustomUserDetails;
// import BookPick.mvp.domain.user.dto.base.UserReq;
// import BookPick.mvp.domain.user.dto.base.UserRes;
// import BookPick.mvp.domain.user.dto.base.delete.UserSoftDeleteRes;
// import BookPick.mvp.domain.user.enums.user.UserSuccessCode;
// import BookPick.mvp.domain.user.exception.common.UserNameNotNullException;
// import BookPick.mvp.domain.user.service.base.UserService;
// import BookPick.mvp.domain.user.util.AdminManager;
// import BookPick.mvp.global.api.ApiResponse;
// import io.swagger.v3.oas.annotations.Operation;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.web.bind.annotation.*;
//
// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/api/v1")
// public class UserController {
//
//    private final UserService userService;
//    private final AdminManager adminManager;
//
//    // 유저 생성
//    @PostMapping
//    @Operation(summary = "유저 추가", description = "새로운 유저를 추가합니다", tags = {"User"})
//    public ResponseEntity<ApiResponse<UserRes>> createUser(@AuthenticationPrincipal
// CustomUserDetails currentUser,
//                                                           @RequestBody @Valid UserReq req) {
//        if (adminManager.isAdmin(currentUser.getAuthorities())) {
//            throw new UserNameNotNullException();
//        }
//
//        UserRes res = userService.CreateUser(req);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.success(UserSuccessCode.CREATE_USER_SUCCESS, res));
//    }
//
//    // 유저 조회
//    @GetMapping
//    @Operation(summary = "유저 프로필 조회", description = "로그인한 사용자의 프로필을 조회합니다.", tags = {"User"})
//    public ResponseEntity<ApiResponse<UserRes>> getUseProfile(@AuthenticationPrincipal
// CustomUserDetails currentUser) {
//        UserRes res = userService.userProfileGet(currentUser.getId());
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(ApiResponse.success(UserSuccessCode.GET_MY_PROFILE_SUCCESS, res));
//    }
//
//
//
//    // 유저 수정
//    @PatchMapping
//    @Operation(summary = "유저 프로필 수정", description = "로그인한 사용자의 프로필을 수정합니다.", tags = {"User"})
//    public ResponseEntity<ApiResponse<UserRes>> updateUserProfile(@AuthenticationPrincipal
// CustomUserDetails currentUser
//    , @RequestBody @Valid UserReq req) {
//        UserRes res = userService.userProfileUpdate(currentUser.getId(), req);
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(ApiResponse.success(UserSuccessCode.UPDATE_MY_PROFILE_SUCCESS, res));
//    }
//
//
//    // 유저 소프트 삭제
//    @DeleteMapping("/soft")
//    @Operation(summary = "유저 프로필 소프트 삭제", description = "로그인한 사용자의 프로필을 임시 삭제합니다.", tags =
// {"User"})
//    public ResponseEntity<ApiResponse<UserSoftDeleteRes>> softDeleteUser(@AuthenticationPrincipal
// CustomUserDetails currentUser) {
//        UserSoftDeleteRes res = userService.softDeleteProfile(currentUser.getId());
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(ApiResponse.success(UserSuccessCode.GET_MY_PROFILE_SUCCESS, res));
//    }
//
//
//    // 유저 하드 삭제
//    @DeleteMapping("/hard")
//    @Operation(summary = "유저 프로필 하드 삭제", description = "로그인한 사용자의 프로필을 완전히 삭제합니다.", tags =
// {"User"})
//    public ResponseEntity<ApiResponse<UserRes>> hardDeleteUser(@AuthenticationPrincipal
// CustomUserDetails currentUser) {
//        if (adminManager.isAdmin(currentUser.getAuthorities())) {
//            userService.hardDeleteUserProfile(currentUser.getId());
//        }
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(ApiResponse.success(UserSuccessCode.GET_MY_PROFILE_SUCCESS, null));
//    }
// }
