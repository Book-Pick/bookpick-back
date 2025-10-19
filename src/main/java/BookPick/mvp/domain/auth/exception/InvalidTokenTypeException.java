package BookPick.mvp.domain.auth.exception;

import BookPick.mvp.global.api.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class InvalidTokenTypeException extends BusinessException {
    public InvalidTokenTypeException(){
        super(ErrorCode.Invalid_Token_Type);
    }
}
