package BookPick.mvp.global.enums;

import org.springframework.http.HttpStatus;

public interface SuccessCodeInterface {
    public HttpStatus getStatus();

    public String getMessage();
}
