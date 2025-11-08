package BookPick.mvp.global.api.ErrorCode;

import org.springframework.http.HttpStatus;

public interface ErrorCodeInterface {
    HttpStatus getStatus();
    String getMessage();
}
