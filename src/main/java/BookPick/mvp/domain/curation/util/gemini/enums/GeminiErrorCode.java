package BookPick.mvp.domain.curation.util.gemini.enums;

import BookPick.mvp.global.enums.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GeminiErrorCode implements ErrorCodeInterface {
    GEMINI_API_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Gemini API 호출에 실패했습니다."),
    GEMINI_API_RATE_LIMITED(HttpStatus.TOO_MANY_REQUESTS, "요청이 너무 많습니다. 잠시 후 다시 시도해주세요.");

    private final HttpStatus status;
    private final String message;
}
