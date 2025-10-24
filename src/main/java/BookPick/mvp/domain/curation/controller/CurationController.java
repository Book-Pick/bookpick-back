// CurationController.java에 추가
package BookPick.mvp.domain.curation.controller;

import BookPick.mvp.domain.auth.service.MyUserDetailsService.*;
import BookPick.mvp.domain.curation.dto.create.CurationCreateReq;
import BookPick.mvp.domain.curation.dto.create.CurationCreateRes;
import BookPick.mvp.domain.curation.dto.get.CurationGetRes;
import BookPick.mvp.domain.curation.dto.update.CurationUpdateReq;
import BookPick.mvp.domain.curation.dto.update.CurationUpdateRes;
import BookPick.mvp.domain.curation.dto.delete.CurationDeleteRes;
import BookPick.mvp.domain.curation.service.CurationService;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/curations")
@RequiredArgsConstructor
public class CurationController {

    private final CurationService curationService;

    // -- 큐레이션 생성 --
    @PostMapping
    public ResponseEntity<ApiResponse<CurationCreateRes>> create(
            @Valid @RequestBody CurationCreateReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        CurationCreateRes res = curationService.create(currentUser.getId(), req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.CURATION_REGISTER_SUCCESS, res));
    }

    // -- 큐레이션 단건 조회 --
    @GetMapping("/{curationId}")
    public ResponseEntity<ApiResponse<CurationGetRes>> getCuration(
            @PathVariable Long curationId) {
        CurationGetRes res = curationService.findCuration(curationId);

        return ResponseEntity.ok()
                .body(ApiResponse.success(SuccessCode.CURATION_GET_SUCCESS, res));
    }

    // -- 큐레이션 수정 --
    @PatchMapping("/{curationId}")
    public ResponseEntity<ApiResponse<CurationUpdateRes>> updateCuration(
            @PathVariable Long curationId,
            @Valid @RequestBody CurationUpdateReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        CurationUpdateRes res = curationService.modifyCuration(currentUser.getId(), curationId, req);

        return ResponseEntity.ok()
                .body(ApiResponse.success(SuccessCode.CURATION_UPDATE_SUCCESS, res));
    }

    // -- 큐레이션 삭제 --
    @DeleteMapping("/{curationId}")
    public ResponseEntity<ApiResponse<CurationDeleteRes>> deleteCuration(
            @PathVariable Long curationId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        CurationDeleteRes res = curationService.removeCuration(currentUser.getId(), curationId);

        return ResponseEntity.ok()
                .body(ApiResponse.success(SuccessCode.CURATION_DELETE_SUCCESS, res));
    }
}