package BookPick.mvp.domain.ReadingPreference.enums.resCode;

import BookPick.mvp.global.enums.ErrorCodeInterface;
import BookPick.mvp.global.enums.SuccessCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PreferenceSuccessCode implements SuccessCodeInterface {


    PREFERENCE_NOT_FOUND(HttpStatus.OK, "사용자의 독서취향이 설정되지 않았습니다.");

    private final HttpStatus status;
    private final String message;
}
