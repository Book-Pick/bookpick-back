package BookPick.mvp.domain.user.exception;

import BookPick.mvp.domain.user.enums.UserErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class PasswordMismatchException extends BusinessException {
    public PasswordMismatchException() {
        super(UserErrorCode.PASSWORD_MISMATCH);
    }
}
