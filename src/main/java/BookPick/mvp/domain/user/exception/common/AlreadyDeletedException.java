package BookPick.mvp.domain.user.exception.common;

import BookPick.mvp.domain.user.enums.user.UserErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class AlreadyDeletedException extends BusinessException {
    public AlreadyDeletedException(){
        super(UserErrorCode.ALREADY_DELETE_USR);
    }
}
