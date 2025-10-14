package BookPick.mvp.global.exception;

import BookPick.mvp.global.api.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());  // 나중에 로그 확인을 위해 런타임 예외 디테일 message 에 저장
        this.errorCode=errorCode;
    }
}
