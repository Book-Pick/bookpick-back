package BookPick.mvp.domain.comment.dto.read;

import BookPick.mvp.global.dto.PageInfo;
import java.time.LocalDateTime;
import java.util.List;

// -- R --
public record CommentListRes(List<CommentSummary> comments, PageInfo pageInfo) {
    public static CommentListRes of(List<CommentSummary> comments, PageInfo pageInfo) {
        return new CommentListRes(comments, pageInfo);
    }

    // 📝 댓글 요약 DTO
    public record CommentSummary(
            Long commentId,
            Long userId,
            Long parentId,
            String nickname,
            String profileImageUrl,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        public static CommentSummary of(
                Long commentId,
                Long userId,
                Long parentId,
                String nickname,
                String profileImageUrl,
                String content,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
            return new CommentSummary(
                    commentId,
                    userId,
                    parentId,
                    nickname,
                    profileImageUrl,
                    content,
                    createdAt,
                    updatedAt);
        }
    }
}
