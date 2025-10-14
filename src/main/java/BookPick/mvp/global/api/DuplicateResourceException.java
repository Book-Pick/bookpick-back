package BookPick.mvp.global.api;


import BookPick.mvp.global.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;
import BookPick.mvp.global.api.ErrorCode.*;


@Getter
@Setter
public class DuplicateResourceException extends BusinessException {
        ErrorCode errorCode;

    public DuplicateResourceException(ErrorCode errorCode){
        super(ErrorCode.DUPLICATE_EMAIL); // Throwable의 detailMessage에 message 저장
    }
}
