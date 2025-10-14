package BookPick.mvp.domain.auth.exception;

import BookPick.mvp.global.api.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;


// 401
public class InvalidLoginException extends BusinessException {
    public InvalidLoginException() {
        super(ErrorCode.AUTHENTICATION_FAILED);
    }
}
