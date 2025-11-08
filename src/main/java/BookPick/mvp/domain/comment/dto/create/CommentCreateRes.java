package BookPick.mvp.domain.comment.dto.create;

import BookPick.mvp.domain.comment.entity.Comment;

public record CommentCreateRes(
        Long commentId
) {
    public static CommentCreateRes from(Comment comment) {
        return new CommentCreateRes(
                comment.getId()
        );
    }
}
