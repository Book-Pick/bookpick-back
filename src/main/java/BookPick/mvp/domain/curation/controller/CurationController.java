// CurationController.java에 추가
package BookPick.mvp.domain.curation.controller;

import BookPick.mvp.domain.auth.service.MyUserDetailsService.*;
import BookPick.mvp.domain.curation.SortType;
import BookPick.mvp.domain.curation.dto.create.CurationCreateReq;
import BookPick.mvp.domain.curation.dto.create.CurationCreateRes;
import BookPick.mvp.domain.curation.dto.get.list.CurationListGetRes;
import BookPick.mvp.domain.curation.dto.get.one.CurationGetRes;
import BookPick.mvp.domain.curation.dto.update.CurationUpdateReq;
import BookPick.mvp.domain.curation.dto.update.CurationUpdateRes;
import BookPick.mvp.domain.curation.dto.delete.CurationDeleteRes;
import BookPick.mvp.domain.curation.service.CurationService;
import BookPick.mvp.global.HyperParam.Defaults;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Operation(summary = "큐레이션 생성", description = "새 큐레이션을 생성합니다", tags = {"Curation"})
    @PostMapping
    public ResponseEntity<ApiResponse<CurationCreateRes>> create(
            @Valid @RequestBody CurationCreateReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        CurationCreateRes res = curationService.create(currentUser.getId(), req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.CURATION_REGISTER_SUCCESS, res));
    }

    @Operation(summary = "큐레이션 단건 조회", description = "큐레이션 ID로 단건 조회", tags = {"Curation"})
    @GetMapping("/{curationId}")
    public ResponseEntity<ApiResponse<CurationGetRes>> getCuration(
            @PathVariable Long curationId,
            HttpServletRequest req) {
        CurationGetRes res = curationService.findCuration(curationId, req);
        return ResponseEntity.ok()
                .body(ApiResponse.success(SuccessCode.CURATION_GET_SUCCESS, res));
    }

    @Operation(summary = "큐레이션 목록 조회", description = "큐레이션 목록을 페이징 조회", tags = {"Curation"})
    @GetMapping
    public ResponseEntity<ApiResponse<CurationListGetRes>> getCurationList(
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {
        SortType sortType = SortType.fromValue(sort);
        CurationListGetRes curationListGetRes = curationService.getCurationList(sortType, cursor, size);
        return ResponseEntity.ok()
                .body(ApiResponse.success(SuccessCode.CURATION_LIST_GET_SUCCESS, curationListGetRes));
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

    @Operation(summary = "큐레이션 삭제", description = "큐레이션을 삭제합니다", tags = {"Curation"})
    @DeleteMapping("/{curationId}")
    public ResponseEntity<ApiResponse<CurationDeleteRes>> deleteCuration(
            @PathVariable Long curationId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        Long userId = (currentUser == null) ? 2L : currentUser.getId();
        CurationDeleteRes res = curationService.removeCuration(userId, curationId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(SuccessCode.CURATION_DELETE_SUCCESS, res));
    }
}



