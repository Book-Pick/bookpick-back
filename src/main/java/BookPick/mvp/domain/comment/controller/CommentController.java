package BookPick.mvp.domain.comment.controller;

import BookPick.mvp.domain.auth.service.MyUserDetailsService.*;
import BookPick.mvp.domain.comment.dto.CreateCommentReq;
import BookPick.mvp.domain.comment.dto.CreateCommentRes;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    // 1. create post

    public ResponseEntity<ApiResponse<CreateCommentRes>> createComment(@Valid @RequestBody CreateCommentReq req, @AuthenticationPrincipal CustomUserDetails currentUser){

        CreateCommentRes res =null;


        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.COMMENT_CREATE_SUCCESS, res));
        
    }
}
