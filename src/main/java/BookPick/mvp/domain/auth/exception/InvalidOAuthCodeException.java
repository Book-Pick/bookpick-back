package BookPick.mvp.domain.auth.exception;

import BookPick.mvp.global.api.ErrorCode.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class InvalidOAuthCodeException extends BusinessException {
    public InvalidOAuthCodeException() {
        super(ErrorCode.INVALID_OAUTH_CODE);
    }
}
