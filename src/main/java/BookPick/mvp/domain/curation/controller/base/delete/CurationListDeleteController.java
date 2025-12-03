package BookPick.mvp.domain.curation.controller.base.delete;


import BookPick.mvp.domain.auth.exception.InvalidTokenTypeException;
import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.curation.dto.base.create.CurationCreateReq;
import BookPick.mvp.domain.curation.dto.base.create.CurationCreateRes;
import BookPick.mvp.domain.curation.dto.base.delete.CurationListDeleteReq;
import BookPick.mvp.domain.curation.dto.base.delete.CurationListDeleteRes;
import BookPick.mvp.domain.curation.dto.base.get.one.CurationGetRes;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateReq;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateRes;
import BookPick.mvp.domain.curation.dto.base.delete.CurationDeleteRes;
import BookPick.mvp.domain.curation.enums.common.CurationSuccessCode;
import BookPick.mvp.domain.curation.service.base.CurationService;
import BookPick.mvp.domain.curation.service.base.delete.CurationDeleteService;
import BookPick.mvp.domain.user.util.CurrentUserCheck;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

        currentUserCheck.isValidCurrentUser(currentUser);

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
        currentUserCheck.isValidCurrentUser(currentUser);

        CurationListDeleteRes res = curationDeleteService.removeCurations(currentUser.getId(), req);
        return ResponseEntity.ok()
                .body(ApiResponse.success(CurationSuccessCode.CURATION_LIST_DELETE_SUCCESS, res));
    }
}



