package BookPick.mvp.domain.ReadingPreference.controller;

import BookPick.mvp.domain.ReadingPreference.dto.ETC.Delete.ReadingPreferenceDeleteRes;
import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceReq;
import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceRes;
import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.ReadingPreference.service.ReadingPreferenceService;
import BookPick.mvp.domain.user.util.CurrentUserCheck;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/reading-preference")
@RequiredArgsConstructor
public class ReadingPreferenceController {

    private final ReadingPreferenceService readingPreferenceService;
    private final CurrentUserCheck currentUserCheck;

    @Operation(summary = "독서 취향 생성", description = "사용자의 독서 취향을 등록합니다", tags = {"Reading Preference"})
    @PostMapping
    public ResponseEntity<ApiResponse<ReadingPreferenceRes>> create(
            @Valid @RequestBody ReadingPreferenceReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        ReadingPreferenceRes res = readingPreferenceService.addReadingPreference(currentUser.getId(), req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.READING_PREFERENCE_REGISTER_SUCCESS, res));
    }

    @Operation(summary = "독서 취향 조회", description = "사용자의 독서 취향 상세 조회", tags = {"Reading Preference"})
    @GetMapping
    public ResponseEntity<ApiResponse<ReadingPreferenceRes>> getDetails(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        currentUserCheck.isValidCurrentUser(currentUser);

        ReadingPreferenceRes res = readingPreferenceService.findReadingPreference(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.READING_PREFERENCE_READ_SUCCESS, res));
    }

    @Operation(summary = "독서 취향 수정", description = "사용자의 독서 취향을 수정합니다", tags = {"Reading Preference"})
    @PatchMapping
    public ResponseEntity<ApiResponse<ReadingPreferenceRes>> update(
            @Valid @RequestBody ReadingPreferenceReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        ReadingPreferenceRes res = readingPreferenceService.modifyReadingPreference(currentUser.getId(), req);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.READING_PREFERENCE_UPDATE_SUCCESS, res));
    }

    @Operation(summary = "독서 취향 삭제", description = "사용자의 독서 취향을 삭제합니다", tags = {"Reading Preference"})
    @DeleteMapping
    public ResponseEntity<ApiResponse<ReadingPreferenceDeleteRes>> delete(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        ReadingPreferenceDeleteRes res = readingPreferenceService.removeReadingPreference(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.READING_PREFERENCE_DELETE_SUCCESS, res));
    }
}


