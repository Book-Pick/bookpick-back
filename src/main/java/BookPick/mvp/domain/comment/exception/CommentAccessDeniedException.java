// SelfSubscribeDeniedException.java
package BookPick.mvp.domain.comment.exception;

import BookPick.mvp.global.api.ErrorCode.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class CommentAccessDeniedException extends BusinessException {
    public CommentAccessDeniedException() {
        super(ErrorCode.CURATION_ACCESS_DENIED);
    }
}
