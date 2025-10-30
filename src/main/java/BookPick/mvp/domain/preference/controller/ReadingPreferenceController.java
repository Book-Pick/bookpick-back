package BookPick.mvp.domain.preference.controller;

import BookPick.mvp.domain.preference.dto.Create.ReadingPreferenceCreateReq;
import BookPick.mvp.domain.preference.dto.Create.ReadingPreferenceCreateRes;
import BookPick.mvp.domain.preference.dto.Delete.ReadingPreferenceDeleteRes;
import BookPick.mvp.domain.preference.dto.Get.ReadingPreferenceGetRes;
import BookPick.mvp.domain.preference.dto.Update.ReadingPreferenceUpdateReq;
import BookPick.mvp.domain.preference.dto.Update.ReadingPreferenceUpdateRes;
import BookPick.mvp.domain.preference.service.ReadingPreferenceService;
import BookPick.mvp.domain.auth.service.MyUserDetailsService.*;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/reading-preference")
@RequiredArgsConstructor
public class ReadingPreferenceController {

    private final ReadingPreferenceService readingPreferenceService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReadingPreferenceCreateRes>> create(@Valid @RequestBody ReadingPreferenceCreateReq req,
                                                                          @AuthenticationPrincipal CustomUserDetails currentUser) {
        ReadingPreferenceCreateRes res = readingPreferenceService.addReadingPreference(currentUser.getId(), req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.READING_PREFERENCE_REGISTER_SUCCESS, res));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ReadingPreferenceGetRes>> getDetails(
                                                                           @AuthenticationPrincipal CustomUserDetails currentUser) {
        ReadingPreferenceGetRes res = readingPreferenceService.findReadingPreference(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.READING_PREFERENCE_READ_SUCCESS, res));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<ReadingPreferenceUpdateRes>> update(@Valid @RequestBody ReadingPreferenceUpdateReq req,
                                                                         @AuthenticationPrincipal CustomUserDetails currentUser) {
        ReadingPreferenceUpdateRes res = readingPreferenceService.modifyReadingPreference(currentUser.getId(), req);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.READING_PREFERENCE_UPDATE_SUCCESS, res));

    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<ReadingPreferenceDeleteRes>> delete(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        ReadingPreferenceDeleteRes res = readingPreferenceService.removeReadingPreference(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.READING_PREFERENCE_DELETE_SUCCESS, res));

    }

}


