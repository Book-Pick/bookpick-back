package BookPick.mvp.domain.ReadingPreference.controller;

import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceDtos.*;
import BookPick.mvp.domain.ReadingPreference.service.ReadingPreferenceService;
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
    public ResponseEntity<ApiResponse<ReadingPreferenceRegisterRes>> registerReadingPreference(@Valid @RequestBody ReadingPreferenceRegisterReq req,
                                                                                                                     @AuthenticationPrincipal CustomUserDetails user) {
        ReadingPreferenceRegisterRes res = readingPreferenceService.create(user.getId(), req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.READING_PREFERENCE_REGISTER_SUCCESS, res));
    }
}
