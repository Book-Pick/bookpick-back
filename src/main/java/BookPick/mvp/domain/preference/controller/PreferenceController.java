package BookPick.mvp.domain.preference.controller;

import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.domain.preference.dto.PreferenceDtos.*;
import BookPick.mvp.domain.preference.service.PreferenceService;
import BookPick.mvp.global.api.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/api/users/{id}/preferences")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    @PostMapping
    public ResponseEntity<ApiResponse<PreferenceRes>> create(@PathVariable("id") Long userId, @Valid @RequestBody CreateReq req) {
        PreferenceRes res = preferenceService.create(userId, req);
        URI location = URI.create("/api/users/" + userId + "/preferences");

        return ResponseEntity.created(location)
                .body(ApiResponse.success(SuccessCode.PREFERENCE_CREATED, res));
    }
}
