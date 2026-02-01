package BookPick.mvp.global.exception.custom;

import BookPick.mvp.global.api.ErrorCode.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateResourceException extends RuntimeException {
    ErrorCode errorCode;

    public DuplicateResourceException() {
        super(ErrorCode.DUPLICATE_EMAIL.getMessage());
    }
}
