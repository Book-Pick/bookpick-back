package BookPick.mvp.domain.auth.exception;


import BookPick.mvp.domain.auth.enums.AuthErrorCode;
import BookPick.mvp.global.api.ErrorCode.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class NotAuthenticateUser extends BusinessException {
    public NotAuthenticateUser() {
        super(AuthErrorCode.UNAUTHORIZED);
    }

}
