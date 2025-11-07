package BookPick.mvp.domain.curation.util.gemini.enums;

import BookPick.mvp.global.api.ErrorCode.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GeminiErrorCode implements ErrorCodeInterface {

    GEMINI_API_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Gemini API 호출에 실패했습니다.");



    private final HttpStatus status;
    private final String message;
}