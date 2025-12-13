package BookPick.mvp.domain.ReadingPreference.enums.resCode;

import BookPick.mvp.global.enums.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PreferenceErrorCode implements ErrorCodeInterface {

    PREFERENCE_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 독서취향이 설정되지 않았습니다."),
    WRONG_READING_PREFERENCE_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 독서취향 요청값입니다.");



    private final HttpStatus status;
    private final String message;
}
