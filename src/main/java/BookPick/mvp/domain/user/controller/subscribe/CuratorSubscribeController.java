package BookPick.mvp.domain.user.controller.subscribe;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.user.dto.subscribe.CuratorSubscribeReq;
import BookPick.mvp.domain.user.dto.subscribe.CuratorSubscribeRes;
import BookPick.mvp.domain.curation.enums.common.CurationSuccessCode;
import BookPick.mvp.domain.curation.service.base.CurationService;
import BookPick.mvp.domain.user.enums.curator.CuratorSuccessCode;
import BookPick.mvp.domain.user.service.subscribe.CurationSubscribeService;
import BookPick.mvp.domain.user.util.CurrentUserCheck;
import BookPick.mvp.global.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CuratorSubscribeController {

    private final CurationService curationService;
    private final CurrentUserCheck currentUserCheck;
    private final CurationSubscribeService curationSubscribeService;

    // 1. 큐레이터 구독하기


    @PostMapping("/subscribe")
    @Operation(summary = "큐레이션 구독", description = "큐레이션 구독 버튼을 누릅니다.", tags = {"Curation"})
    public ResponseEntity<ApiResponse<CuratorSubscribeRes>> subscribe(
            @RequestBody @Valid CuratorSubscribeReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser){

        currentUserCheck.isValidCurrentUser(currentUser);

        CuratorSubscribeRes curatorSubscribeRes = curationSubscribeService.subscribe(currentUser.getId(), req);

        if (curatorSubscribeRes.subscribed()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(CuratorSuccessCode.CURATOR_SUBSCRIBE_SUCCESS, curatorSubscribeRes));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(CuratorSuccessCode.CURATOR_SUBSCRIBE_CANCLE_SUCCESS, curatorSubscribeRes));
        }
    }






//    @Operation(summary = "큐레이션 구독 리스트 제공", description = "사용자가 구독한 큐레이터 리스트를 제공합니다.", tags = {"Curation"})
//    @GetMapping("/subscribe/list")
//    public ResponseEntity<ApiResponse<CurationListGetRes>> getSubscribedCurations(
//            @AuthenticationPrincipal @Valid CustomUserDetails currentUser,
//            @RequestParam(required = false) Long cursor,
//            @RequestParam(defaultValue = "10") int size) {
//
//        currentUserCheck.isValidCurrentUser(currentUser);
//        CurationListGetRes curationListGetRes = curationSubscribeService.getSubscribedCurations(currentUser.getId(), cursor, size);
//        return ResponseEntity.ok()
//                .body(ApiResponse.success(CurationSuccessCode.GET_CURATION_SUBSCRIBE_LIST_SUCCESS, curationListGetRes));
//    }
}



