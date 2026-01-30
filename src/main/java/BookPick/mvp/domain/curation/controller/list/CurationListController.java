package BookPick.mvp.domain.curation.controller.list;

import BookPick.mvp.domain.ReadingPreference.enums.resCode.PreferenceSuccessCode;
import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.curation.dto.base.get.list.CurationContentRes;
import BookPick.mvp.domain.curation.dto.base.get.list.CurationIdsReq;
import BookPick.mvp.domain.curation.dto.base.get.list.CurationListGetRes;
import BookPick.mvp.domain.curation.enums.common.SortType;
import BookPick.mvp.domain.curation.exception.common.CurationDraftOwnerException;
import BookPick.mvp.domain.curation.service.list.CurationListService;
import BookPick.mvp.domain.user.util.CurrentUserCheck;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/curations")
@RequiredArgsConstructor
public class CurationListController {

    private final CurationListService curationListService;
    private final CurrentUserCheck currentUserCheck;

    // Todo 1. isDrafted = false 인, 큐레이션 리스트 반환

    @Operation(
            summary = "큐레이션 목록  조회",
            description = "최신순 / 인기순 / 사용자 취향 유사도 순",
            tags = {"Curation"})
    @GetMapping
    public ResponseEntity<ApiResponse<CurationListGetRes>> getCurations(
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean draft,
            @AuthenticationPrincipal @Valid CustomUserDetails currentUser) {

        currentUserCheck.validateLoginUser(currentUser);

        // 1. 분류 기준 정하고
        SortType sortType = SortType.fromValue(sort);

        if (draft && !sortType.equals(SortType.SORT_MY)) {
            throw new CurationDraftOwnerException();
        }

        // 2. 큐레이션 리스트 얻기
        CurationListGetRes curationListGetRes =
                curationListService.getCurations(
                        sortType, cursor, size, draft, currentUser.getId());

        if (curationListGetRes.size() == 0) {
            return ResponseEntity.ok()
                    .body(
                            ApiResponse.success(
                                    PreferenceSuccessCode.PREFERENCE_NOT_FOUND,
                                    curationListGetRes));
        }

        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(
                                SuccessCode.CURATION_LIST_GET_SUCCESS, curationListGetRes));
    }

    @Operation(
            summary = "큐레이션 ID 목록으로 조회",
            description = "curationId 배열을 받아 해당 큐레이션 목록 반환",
            tags = {"Curation"})
    @PostMapping("/by-ids")
    public ResponseEntity<ApiResponse<List<CurationContentRes>>> getCurationsByIds(
            @RequestBody @Valid CurationIdsReq request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        currentUserCheck.validateLoginUser(currentUser);

        List<CurationContentRes> curations =
                curationListService.getCurationsByIds(request.curationIds(), currentUser.getId());

        return ResponseEntity.ok()
                .body(ApiResponse.success(SuccessCode.CURATION_LIST_GET_SUCCESS, curations));
    }
}
