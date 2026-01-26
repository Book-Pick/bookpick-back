package BookPick.mvp.domain.user.exception.common;

import BookPick.mvp.domain.user.enums.user.UserErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class PasswordMismatchException extends BusinessException {
    public PasswordMismatchException() {
        super(UserErrorCode.PASSWORD_MISMATCH);
    }
}
