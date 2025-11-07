package BookPick.mvp.domain.auth.exception;

import BookPick.mvp.global.api.ErrorCode.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class JwtTokenExpiredException extends BusinessException {
     public JwtTokenExpiredException(){
         super(ErrorCode.Token_Expired);
     }
}
