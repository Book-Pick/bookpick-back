package BookPick.mvp.domain.curation.enums;

import BookPick.mvp.global.enums.SuccessCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CurationSuccessCode implements SuccessCodeInterface {

    CREATE_DRAFTED_CURATION_SUCCESS(HttpStatus.CREATED, "큐레이션 임시저장에 성공했습니다");

    private final HttpStatus status;
    private final String message;
}
