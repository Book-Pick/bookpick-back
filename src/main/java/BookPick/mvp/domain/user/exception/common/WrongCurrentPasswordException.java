package BookPick.mvp.domain.user.exception.common;

import BookPick.mvp.domain.user.enums.user.UserErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class WrongCurrentPasswordException extends BusinessException {
    public WrongCurrentPasswordException() {
        super(UserErrorCode.WRONG_CURRENT_PASSWORD);
    }
}
