// CurationListController.java에 추가
package BookPick.mvp.domain.curation.controller.base;

import BookPick.mvp.domain.auth.exception.InvalidTokenTypeException;
import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.curation.dto.base.create.CurationCreateReq;
import BookPick.mvp.domain.curation.dto.base.create.CurationCreateRes;
import BookPick.mvp.domain.curation.dto.base.get.one.CurationGetRes;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateReq;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateRes;
import BookPick.mvp.domain.curation.dto.base.delete.CurationDeleteRes;
import BookPick.mvp.domain.curation.service.base.CurationService;
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
public class CurationController {

    private final CurationService curationService;
    private final CurrentUserCheck currentUserCheck;

    @Operation(summary = "큐레이션 생성", description = "새 큐레이션을 생성합니다", tags = {"Curation"})
    @PostMapping
    public ResponseEntity<ApiResponse<CurationCreateRes>> create(
            @Valid @RequestBody CurationCreateReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        currentUserCheck.isValidCurrentUser(currentUser);

        CurationCreateRes res = curationService.create(currentUser.getId(), req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.CURATION_REGISTER_SUCCESS, res));
    }

    @Operation(summary = "큐레이션 단건 조회", description = "큐레이션 ID로 단건 조회", tags = {"Curation"})
    @GetMapping("/{curationId}")
    public ResponseEntity<ApiResponse<CurationGetRes>> getCuration(
            @PathVariable Long curationId,
            HttpServletRequest req,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        CurationGetRes res = curationService.findCuration(curationId, currentUser, req);
        return ResponseEntity.ok()
                .body(ApiResponse.success(SuccessCode.CURATION_GET_SUCCESS, res));
    }



    @Operation(summary = "큐레이션 수정", description = "큐레이션 정보를 수정", tags = {"Curation"})
    @PatchMapping("/{curationId}")
    public ResponseEntity<ApiResponse<CurationUpdateRes>> updateCuration(
            @PathVariable Long curationId,
            @Valid @RequestBody CurationUpdateReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        CurationUpdateRes res = curationService.modifyCuration(currentUser.getId(), curationId, req);
        return ResponseEntity.ok()
                .body(ApiResponse.success(SuccessCode.CURATION_UPDATE_SUCCESS, res));
    }


}



