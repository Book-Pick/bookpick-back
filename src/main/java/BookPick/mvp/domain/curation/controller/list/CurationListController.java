package BookPick.mvp.domain.curation.controller.list;

import BookPick.mvp.domain.auth.exception.InvalidTokenTypeException;
import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.curation.dto.base.get.list.CurationListGetRes;
import BookPick.mvp.domain.curation.enums.common.SortType;
import BookPick.mvp.domain.curation.service.list.CurationListService;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/curations")
@RequiredArgsConstructor
public class CurationListController {

    private final CurationListService curationListService;


    @Operation(summary = "큐레이션 목록  조회", description = "최신순 / 인기순 / 사용자 취향 유사도 순", tags = {"Curation"})
    @GetMapping
    public ResponseEntity<ApiResponse<CurationListGetRes>> getCurations(
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal @Valid CustomUserDetails currentUser
    ) {

        if(currentUser==null){
            throw new InvalidTokenTypeException();
        }

        // 1. SortType 변환
        SortType sortType = SortType.fromValue(sort);

        // 2. 큐레이션 리스트 반환
        CurationListGetRes curationListGetRes = curationListService.getCurations(sortType, cursor, size, currentUser.getId());

        return ResponseEntity.ok()
                .body(ApiResponse.success(SuccessCode.CURATION_LIST_GET_SUCCESS, curationListGetRes));
    }






}



