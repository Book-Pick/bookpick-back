package BookPick.mvp.domain.user.exception;

import BookPick.mvp.global.api.ErrorCode.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(){
        super(ErrorCode.User_NOT_FOUND);
    }

}
