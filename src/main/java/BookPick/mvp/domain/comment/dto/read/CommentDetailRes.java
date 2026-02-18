package BookPick.mvp.domain.comment.dto.read;

import BookPick.mvp.domain.comment.entity.Comment;
import java.time.LocalDateTime;

public record CommentDetailRes(
        Long commentId,
        Long parentId,
        Long curationId,
        Long userId,
        String nickname,
        String profileImageUrl,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static CommentDetailRes of(Comment comment) {
        return new CommentDetailRes(
                comment.getId(),
                comment.getParent() != null ? comment.getParent().getId() : null,
                comment.getCuration().getId(),
                comment.getUser().getId(),
                comment.getUser().getNickname(),
                comment.getUser().getProfileImageUrl(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt());
    }
}
