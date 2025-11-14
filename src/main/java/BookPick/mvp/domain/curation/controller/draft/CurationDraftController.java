package BookPick.mvp.domain.curation.controller.draft;

import BookPick.mvp.domain.auth.exception.NotAuthenticateUser;
import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.curation.dto.base.CurationReq;
import BookPick.mvp.domain.curation.dto.base.CurationRes;
import BookPick.mvp.domain.curation.dto.base.get.one.CurationGetRes;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.enums.CurationSuccessCode;
import BookPick.mvp.domain.curation.service.draft.CurationDraftService;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/curation/draft")
@RequiredArgsConstructor
public class CurationDraftController {

    private final CurationDraftService curationDraftService;

    // 1. 임시 저장 컨트롤러
    // 확장성 : 임시저장, 드래프트 공유 및 유효기간 관리 등
    @PostMapping
    @Operation(summary = "게시글 임시 저장", description = "유저가 작성한 큐레이션을 임시저장합니다", tags = {"Curation"})
    public ResponseEntity<ApiResponse<CurationRes>> saveDraft(@AuthenticationPrincipal CustomUserDetails currentUser, @RequestBody @Valid CurationReq req) {


        // 1) 로그인 만료 검사
        if (currentUser == null) {
            throw new NotAuthenticateUser();
        }

        CurationRes res = curationDraftService.draftSave(currentUser.getId(), req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(CurationSuccessCode.CREATE_DRAFTED_CURATION_SUCCESS, res));
    }


    // 2. 임시 저장 조회 컨트롤러
//    @Operation(summary = "임시저장 단건 조회", description = "작성자 임시저장 큐레이션 단건 조회", tags = {"Curation"})
//    @GetMapping("/{curationId}")
//    public ResponseEntity<ApiResponse<CurationGetRes>> getCuration(
//            @PathVariable Long curationId,
//            HttpServletRequest req) {
//
//        // 1) 로그인 만료 검사
//        if (currentUser == null) {
//            throw new NotAuthenticateUser();
//        }
//        CurationGetRes res = curationService.findCuration(curationId, req);
//        return ResponseEntity.ok()
//                .body(ApiResponse.success(SuccessCode.CURATION_GET_SUCCESS, res));
//    }

}
