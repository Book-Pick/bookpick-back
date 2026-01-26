package BookPick.mvp.global.enums;

import org.springframework.http.HttpStatus;

public interface ErrorCodeInterface {
    HttpStatus getStatus();
    String getMessage();
}

