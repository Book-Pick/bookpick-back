package BookPick.mvp.domain.curation.controller.like;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.curation.dto.base.create.CurationCreateReq;
import BookPick.mvp.domain.curation.dto.base.create.CurationCreateRes;
import BookPick.mvp.domain.curation.enums.CurationSuccessCode;
import BookPick.mvp.domain.curation.service.like.CurationLikeService;
import BookPick.mvp.domain.user.util.CurrentUserCheck;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/curations/like")
@RequiredArgsConstructor
public class CurationLikeController {

    private final CurationLikeService curationLikeService;
    private final CurrentUserCheck currentUserCheck;


    @GetMapping("/{curationId}")
    @Operation(summary = "큐레이션 좋아요", description = "큐레이션 좋아요 버튼을 누릅니다.", tags = {"Curation"})
    public ResponseEntity<ApiResponse<Void>> likeOrUnlikeCuration(@AuthenticationPrincipal CustomUserDetails currentUser
            , @PathVariable Long curationId) {

        currentUserCheck.isValidCurrentUser(currentUser);

        boolean liked = curationLikeService.CurationLikeOrUnlike(currentUser.getId(), curationId);

        if (liked) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(CurationSuccessCode.POST_LIKE_SUCCESS, null));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(CurationSuccessCode.POST_DISLIKE_SUCCESS, null));
        }
    }





}
