package BookPick.mvp.domain.curation.controller.base.delete;


import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.curation.dto.base.delete.CurationListDeleteReq;
import BookPick.mvp.domain.curation.dto.base.delete.CurationListDeleteRes;
import BookPick.mvp.domain.curation.dto.base.delete.CurationDeleteRes;
import BookPick.mvp.domain.curation.enums.common.CurationSuccessCode;
import BookPick.mvp.domain.curation.service.base.delete.CurationDeleteService;
import BookPick.mvp.domain.user.util.CurrentUserCheck;
import BookPick.mvp.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/curations")
@RequiredArgsConstructor
public class CurationListDeleteController {

    private final CurationDeleteService curationDeleteService;
    private final CurrentUserCheck currentUserCheck;

    @Operation(summary = "큐레이션 삭제", description = "큐레이션을 삭제합니다", tags = {"Curation"})
    @DeleteMapping("/{curationId}")
    public ResponseEntity<ApiResponse<CurationDeleteRes>> deleteCuration(
            @PathVariable Long curationId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        currentUserCheck.validateLoginUser(currentUser);

        CurationDeleteRes res = curationDeleteService.removeCuration(currentUser.getId(), curationId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(CurationSuccessCode.CURATION_DELETE_SUCCESS, res));
    }

    @Operation(summary = "큐레이션 리스트 삭제", description = "복수의 큐레이션들을 삭제합니다", tags = {"Curation"})
    @DeleteMapping
    public ResponseEntity<ApiResponse<CurationListDeleteRes>> deleteCurations(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody CurationListDeleteReq req
    ) {
        currentUserCheck.validateLoginUser(currentUser);

        CurationListDeleteRes res = curationDeleteService.removeCurations(currentUser.getId(), req);
        return ResponseEntity.ok()
                .body(ApiResponse.success(CurationSuccessCode.CURATION_LIST_DELETE_SUCCESS, res));
    }
}



