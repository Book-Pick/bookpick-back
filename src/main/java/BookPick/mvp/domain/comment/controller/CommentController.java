package BookPick.mvp.domain.comment.controller;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.comment.dto.create.CommentCreateReq;
import BookPick.mvp.domain.comment.dto.create.CommentCreateRes;
import BookPick.mvp.domain.comment.dto.delete.CommentDeleteRes;
import BookPick.mvp.domain.comment.dto.read.CommentDetailRes;
import BookPick.mvp.domain.comment.dto.read.CommentListRes;
import BookPick.mvp.domain.comment.dto.update.CommentUpdateReq;
import BookPick.mvp.domain.comment.dto.update.CommentUpdateRes;
import BookPick.mvp.domain.comment.service.CommentService;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/curations")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // -- 1. 댓글 생성 --
    @PostMapping("/{curationId}/comments")
    public ResponseEntity<ApiResponse<CommentCreateRes>> create(@PathVariable Long curationId,
                                                                @RequestBody CommentCreateReq commentCreateReq, @AuthenticationPrincipal CustomUserDetails currentUser) {
        CommentCreateRes res = commentService.createComment(currentUser.getId(), curationId, commentCreateReq);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.COMMENT_CREATE_SUCCESS, res));
    }

    // -- 2. 댓글 조회 --
    @GetMapping("/{curationId}/comments")
    public ResponseEntity<ApiResponse<CommentListRes>> getCommentList(@PathVariable Long curationId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        CommentListRes res = commentService.getCommentList(curationId, page, size);

        if (res.comments().isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(SuccessCode.COMMENT_LIST_EMPTY, res));
        }

        return ResponseEntity.ok(ApiResponse.success(SuccessCode.COMMENT_LIST_READ_SUCCESS, res));
    }
    @GetMapping("/{curationId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentDetailRes>> getCommentDetail(@PathVariable Long curationId ,@PathVariable Long commentId) {
        CommentDetailRes res = commentService.getCommentDetail(commentId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.COMMENT_READ_SUCCESS, res));
    }


    // -- 3. 댓글 수정 --
    @PatchMapping("/{curationId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentUpdateRes>> updateComment(
            @PathVariable Long curationId,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateReq req
    ) {
        CommentUpdateRes res = commentService.updateComment(commentId, req);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.COMMENT_UPDATE_SUCCESS, res));
    }


    // -- 4. 댓글 삭제 --
    @DeleteMapping("/{curationId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentDeleteRes>> deleteComment(
            @PathVariable Long curationId,
            @PathVariable Long commentId
    ) {
        CommentDeleteRes res = commentService.deleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.COMMENT_DELETE_SUCCESS, res));
    }


}





