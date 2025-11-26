// CurationListController.java에 추가
package BookPick.mvp.domain.curation.controller.subscribe;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.curation.dto.subscribe.CurationSubscribeDto;
import BookPick.mvp.domain.curation.enums.CurationSuccessCode;
import BookPick.mvp.domain.curation.service.base.CurationService;
import BookPick.mvp.domain.curation.service.subscribe.CurationSubscribeService;
import BookPick.mvp.domain.user.util.CurrentUserCheck;
import BookPick.mvp.global.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/curations")
@RequiredArgsConstructor
public class CurationSubscribeController {

    private final CurationService curationService;
    private final CurrentUserCheck currentUserCheck;
    private final CurationSubscribeService curationSubscribeService;

    @PostMapping("/{curationId}/subscribe")
    @Operation(summary = "큐레이션 구독", description = "큐레이션 구독 버튼을 누릅니다.", tags = {"Curation"})
    public ResponseEntity<ApiResponse<CurationSubscribeDto>> likeOrUnlikeCuration(
            @AuthenticationPrincipal CustomUserDetails currentUser
            , @PathVariable Long curationId) {

        currentUserCheck.isValidCurrentUser(currentUser);

        CurationSubscribeDto curationSubscribeDto = curationSubscribeService.subscribe(currentUser.getId(), curationId);

        if (curationSubscribeDto.subscribed()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(CurationSuccessCode.CURATION_SUBSCRIBE_SUCCESS, curationSubscribeDto));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(CurationSuccessCode.CURATION_SUBSCRIBE_CANCLE_SUCCESS, curationSubscribeDto));
        }
    }



//    @Operation(summary = "큐레이션 구독 리스트 제공", description = "사용자가 구독한 큐레이터 리스트를 제공합니다.", tags = {"Curation"})
//    @PatchMapping("/{curationId}")
//    public ResponseEntity<ApiResponse<CurationUpdateRes>> updateCuration(
//            @PathVariable Long curationId,
//            @Valid @RequestBody CurationUpdateReq req,
//            @AuthenticationPrincipal CustomUserDetails currentUser) {
//        CurationUpdateRes res = curationService.modifyCuration(currentUser.getId(), curationId, req);
//        return ResponseEntity.ok()
//                .body(ApiResponse.success(SuccessCode.CURATION_UPDATE_SUCCESS, res));
//    }


}



