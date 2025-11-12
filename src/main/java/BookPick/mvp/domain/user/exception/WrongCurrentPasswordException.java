package BookPick.mvp.domain.user.exception;


import BookPick.mvp.domain.user.enums.UserErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class WrongCurrentPasswordException extends BusinessException {
    public WrongCurrentPasswordException(){
        super(UserErrorCode.WRONG_CURRENT_PASSWORD);
    }
}
