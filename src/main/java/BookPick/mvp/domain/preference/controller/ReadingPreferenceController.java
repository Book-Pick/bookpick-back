package BookPick.mvp.domain.preference.controller;

import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.domain.preference.dto.ReadingPreference.*;
import BookPick.mvp.domain.preference.service.ReadingPreferenceService;
import BookPick.mvp.global.api.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users/{id}/preferences")
@RequiredArgsConstructor
public class ReadingPreferenceController {

    private final ReadingPreferenceService readingPreferenceService;

    @PostMapping
    public ResponseEntity<ApiResponse<PreferenceRes>> create(@PathVariable("id") Long userId, @Valid @RequestBody CreateReq req) {
        PreferenceRes res = readingPreferenceService.create(userId, req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.COMMENT_CREATE_SUCCESS, res));
    }
}
