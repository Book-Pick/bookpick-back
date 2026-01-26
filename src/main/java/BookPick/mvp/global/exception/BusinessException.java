package BookPick.mvp.global.exception;

import BookPick.mvp.global.enums.ErrorCodeInterface;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final ErrorCodeInterface errorCode;


    public BusinessException(ErrorCodeInterface errorCode){
        super(errorCode.getMessage());  // 나중에 로그 확인을 위해 런타임 예외 디테일 message 에 저장
        this.errorCode=errorCode;
    }
}
