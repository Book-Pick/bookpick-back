// CurationListController.java에 추가
package BookPick.mvp.domain.curation.controller.base;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.curation.dto.base.CurationReq;
import BookPick.mvp.domain.curation.dto.base.create.CurationCreateRes;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateReq;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateRes;
import BookPick.mvp.domain.curation.service.base.CurationService;
import BookPick.mvp.domain.curation.service.base.create.CurationCreateService;
import BookPick.mvp.domain.user.util.CurrentUserCheck;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
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
    private final CurationCreateService curationCreateService;

    private final CurrentUserCheck currentUserCheck;

    @Operation(summary = "큐레이션 생성", description = "새 큐레이션을 생성합니다", tags = {"Curation"})
    @PostMapping
    public ResponseEntity<ApiResponse<CurationCreateRes>> create(
            @Valid @RequestBody CurationReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        currentUserCheck.validateLoginUser(currentUser);

        CurationCreateRes res = curationCreateService.createCuration(currentUser.getId(), req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.CURATION_REGISTER_SUCCESS, res));
    }





    @Operation(summary = "큐레이션 수정", description = "큐레이션 정보를 수정", tags = {"Curation"})
    @PatchMapping("/{curationId}")
    public ResponseEntity<ApiResponse<CurationUpdateRes>> updateCuration(
            @PathVariable Long curationId,
            @Valid @RequestBody CurationUpdateReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        currentUserCheck.validateLoginUser(currentUser);
        
        CurationUpdateRes res = curationService.curationUpdate(currentUser.getId(), curationId, req);
        return ResponseEntity.ok()
                .body(ApiResponse.success(SuccessCode.CURATION_UPDATE_SUCCESS, res));
    }


}



