package BookPick.mvp.domain.curation.controller;

import BookPick.mvp.domain.auth.service.MyUserDetailsService.*;
import BookPick.mvp.domain.curation.dto.create.CurationCreateReq;
import BookPick.mvp.domain.curation.dto.create.CurationCreateRes;
import BookPick.mvp.domain.curation.service.CurationService;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/curations")
@RequiredArgsConstructor
public class CurationController {

    private final CurationService curationService;

    // -- 큐레이션 생성 --
    @PostMapping
    public ResponseEntity<ApiResponse<CurationCreateRes>> create(@Valid @RequestBody CurationCreateReq req, @AuthenticationPrincipal CustomUserDetails currentUser) {
        CurationCreateRes res = curationService.create(currentUser.getId(), req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.CURATION_REGISTER_SUCCESS, res));

    }


}
