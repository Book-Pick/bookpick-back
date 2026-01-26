package BookPick.mvp.domain.comment.controller.base;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.comment.service.PagenationService;
import BookPick.mvp.domain.comment.dto.create.CommentCreateReq;
import BookPick.mvp.domain.comment.dto.create.CommentCreateRes;
import BookPick.mvp.domain.comment.dto.delete.CommentDeleteRes;
import BookPick.mvp.domain.comment.dto.read.CommentDetailRes;
import BookPick.mvp.domain.comment.dto.read.CommentListRes;
import BookPick.mvp.domain.comment.dto.update.CommentUpdateReq;
import BookPick.mvp.domain.comment.dto.update.CommentUpdateRes;
import BookPick.mvp.domain.comment.service.CommentService;
import BookPick.mvp.domain.user.util.CurrentUserCheck;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
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
    private final PagenationService pagenationService;
    private final CurrentUserCheck currentUserCheck;

    // -- 1. 댓글 생성 --
    @Operation(summary = "댓글 생성", description = "특정 큐레이션에 댓글을 생성합니다", tags = {"Comment"})
    @PostMapping("/{curationId}/comments")
    public ResponseEntity<ApiResponse<CommentCreateRes>> create(@PathVariable Long curationId,
                                                                @RequestBody CommentCreateReq commentCreateReq,
                                                                @AuthenticationPrincipal CustomUserDetails currentUser) {

//        currentUserCheck.validateLoginUser(currentUser);

        CommentCreateRes res = commentService.createComment(currentUser.getId(), curationId, commentCreateReq);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.COMMENT_CREATE_SUCCESS, res));
    }

    // -- 2. 댓글 리스트 조회 --
    @Operation(summary = "댓글 리스트 조회", description = "특정 큐레이션의 댓글 목록을 페이지네이션하여 조회합니다", tags = {"Comment"})
    @GetMapping("/{curationId}/comments")
    public ResponseEntity<ApiResponse<CommentListRes>> getCommentList(@PathVariable Long curationId,
                                                                      @RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "20") int size) {


        // 1. 페이지를 1부터 보여주기 위해 -1
        page = page - 1;

        // 2. -1한 페이지가 0보다 작으면 0으로
        page = pagenationService.changeMinusPageToZeroPage(page);
        CommentListRes res = commentService.getCommentList(curationId, page, size);
        if (res.comments().isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(SuccessCode.COMMENT_LIST_EMPTY, res));
        }

        return ResponseEntity.ok(ApiResponse.success(SuccessCode.COMMENT_LIST_READ_SUCCESS, res));
    }


    // 2.1 댓글 상세 조회
    @Operation(summary = "댓글 상세 조회", description = "특정 댓글의 상세 정보를 조회합니다", tags = {"Comment"})
    @GetMapping("/{curationId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentDetailRes>> getCommentDetail(@PathVariable Long curationId, @PathVariable Long commentId) {
        CommentDetailRes res = commentService.getCommentDetail(commentId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.COMMENT_READ_SUCCESS, res));
    }


    // -- 3. 댓글 수정 --
    @Operation(summary = "댓글 수정", description = "특정 댓글의 내용을 수정합니다", tags = {"Comment"})
    @PatchMapping("/{curationId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentUpdateRes>> updateComment(
            @PathVariable Long curationId,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
//        currentUserCheck.validateLoginUser(currentUser);

        CommentUpdateRes res = commentService.updateComment(currentUser.getId(), commentId, req);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.COMMENT_UPDATE_SUCCESS, res));
    }


    // -- 4. 댓글 삭제 --
    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다 (자식 댓글도 함께 삭제됩니다)", tags = {"Comment"})
    @DeleteMapping("/{curationId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentDeleteRes>> deleteComment(
            @PathVariable Long curationId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        CommentDeleteRes res = commentService.deleteComment(currentUser.getId(), curationId, commentId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.COMMENT_DELETE_SUCCESS, res));
    }


}





