package BookPick.mvp.domain.user.exception.profile;

import BookPick.mvp.domain.user.enums.user.UserErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class UserNameNotNullException extends BusinessException {
    public UserNameNotNullException(){
        super(UserErrorCode.USER_NAME_IS_NULL);
    }
}
