package BookPick.mvp.domain.comment.dto.delete;

import java.time.LocalDateTime;

// -- D --
public record CommentDeleteRes(
        Long commentId,
        LocalDateTime deletedAt
) {
    public static CommentDeleteRes of(Long commentId, LocalDateTime deletedAt) {
        return new CommentDeleteRes(commentId, deletedAt);
    }
}
