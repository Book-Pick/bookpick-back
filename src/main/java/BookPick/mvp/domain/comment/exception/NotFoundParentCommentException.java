package BookPick.mvp.domain.comment.exception;

import BookPick.mvp.global.api.ErrorCode.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class NotFoundParentCommentException extends BusinessException {
    public NotFoundParentCommentException() {
        super(ErrorCode.PARENTS_COMMENT_NOT_FOUND);
    }
}
