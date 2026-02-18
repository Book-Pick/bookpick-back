package BookPick.mvp.domain.comment.controller.all;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.comment.dto.read.ReceivedCommentsDTO;
import BookPick.mvp.domain.comment.service.ReceivedCommentsService;
import BookPick.mvp.domain.user.util.CurrentUserCheck;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class ReceivedCommentsController {

    private final ReceivedCommentsService receivedCommentsService;
    private final CurrentUserCheck currentUserCheck;

    @Operation(
            summary = "받은 댓글 조회",
            description = "현재 사용자가 받은 최신 댓글 목록을 조회합니다",
            tags = {"Comment"})
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "받은 댓글 조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요합니다", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponse<ReceivedCommentsDTO>> getReceivedComments(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        currentUserCheck.validateLoginUser(currentUser);

        ReceivedCommentsDTO res = receivedCommentsService.receivedCommentsRead(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.COMMENT_LIST_READ_SUCCESS, res));
    }
}
