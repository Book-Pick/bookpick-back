package BookPick.mvp.domain.curation.Controller;

import BookPick.mvp.domain.auth.service.MyUserDetailsService.*;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/curations")
public class CurationController {

    @PostMapping
    public ResponseEntity<ApiResponse<CurationCreateRes>> create(@Valid @RequestBody CurationCreateReq req, @AuthenticationPrincipal CustomUserDetails currentUser){
        CurationCreateRes res = curationRepository.create(currentUser.getId(), req);

        return ApiResponse.success(SuccessCode.READING_PREFERENCE_UPDATE_SUCCESS, res);

    }

    @PostMapping
    public ResponseEntity<ApiResponse<CurationCreateRes>> create(@Valid @RequestBody CurationCreateReq req, @AuthenticationPrincipal CustomUserDetails currentUser){
        CurationCreateRes res = curationRepository.create(currentUser.getId(), req);

        return ApiResponse.success(SuccessCode.READING_PREFERENCE_UPDATE_SUCCESS, res);

    }

    @PostMapping
    public ResponseEntity<ApiResponse<CurationCreateRes>> create(@Valid @RequestBody CurationCreateReq req, @AuthenticationPrincipal CustomUserDetails currentUser){
        CurationCreateRes res = curationRepository.create(currentUser.getId(), req);

        return ApiResponse.success(SuccessCode.READING_PREFERENCE_UPDATE_SUCCESS, res);

    }

    @PostMapping
    public ResponseEntity<ApiResponse<CurationCreateRes>> create(@Valid @RequestBody CurationCreateReq req, @AuthenticationPrincipal CustomUserDetails currentUser){
        CurationCreateRes res = curationRepository.create(currentUser.getId(), req);

        return ApiResponse.success(SuccessCode.READING_PREFERENCE_UPDATE_SUCCESS, res);

    }

    @PostMapping
    public ResponseEntity<ApiResponse<CurationCreateRes>> create(@Valid @RequestBody CurationCreateReq req, @AuthenticationPrincipal CustomUserDetails currentUser){
        CurationCreateRes res = curationRepository.create(currentUser.getId(), req);

        return ApiResponse.success(SuccessCode.READING_PREFERENCE_UPDATE_SUCCESS, res);

    }
}
