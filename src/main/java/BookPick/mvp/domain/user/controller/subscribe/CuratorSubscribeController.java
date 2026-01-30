package BookPick.mvp.domain.user.controller.subscribe;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.user.dto.subscribe.CuratorSubscribeReq;
import BookPick.mvp.domain.user.dto.subscribe.CuratorSubscribeRes;
import BookPick.mvp.domain.user.dto.subscribe.SubscribedCuratorPageRes;
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

    private final CurrentUserCheck currentUserCheck;
    private final CurationSubscribeService curationSubscribeService;

    // 1. 큐레이터 구독하기

    @PostMapping("/subscribe")
    @Operation(
            summary = "큐레이터 구독/취소",
            description = "큐레이터 구독 버튼을 누릅니다. 이미 구독된 상태라면 구독이 취소됩니다.",
            tags = {"Subscribe"})
    public ResponseEntity<ApiResponse<CuratorSubscribeRes>> subscribe(
            @RequestBody @Valid CuratorSubscribeReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        currentUserCheck.validateLoginUser(currentUser);

        CuratorSubscribeRes curatorSubscribeRes =
                curationSubscribeService.subscribe(currentUser.getId(), req);

        if (curatorSubscribeRes.subscribed()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            ApiResponse.success(
                                    CuratorSuccessCode.CURATOR_SUBSCRIBE_SUCCESS,
                                    curatorSubscribeRes));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            ApiResponse.success(
                                    CuratorSuccessCode.CURATOR_SUBSCRIBE_CANCLE_SUCCESS,
                                    curatorSubscribeRes));
        }
    }

    @Operation(
            summary = "큐레이터 구독 리스트 제공",
            description = "사용자가 구독한 큐레이터 리스트를 제공합니다.",
            tags = {"Subscribe"})
    @GetMapping("/subscribe/curators")
    public ResponseEntity<ApiResponse<SubscribedCuratorPageRes>> getSubscribedCurators(
            @AuthenticationPrincipal @Valid CustomUserDetails currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        currentUserCheck.validateLoginUser(currentUser);
        SubscribedCuratorPageRes subscribedCuratorPageRes =
                curationSubscribeService.getSubscribedCurators(currentUser.getId(), page, size);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(
                                CuratorSuccessCode.GET_CURATOR_SUBSCRIBE_LIST_SUCCESS,
                                subscribedCuratorPageRes));
    }
}
