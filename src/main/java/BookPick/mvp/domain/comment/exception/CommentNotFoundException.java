package BookPick.mvp.domain.comment.exception;


import BookPick.mvp.global.api.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class CommentNotFoundException extends BusinessException {
    public CommentNotFoundException(){
        super(ErrorCode.COMMENT_NOT_FOUND);
    }

}
