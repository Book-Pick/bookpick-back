package BookPick.mvp.domain.curation.enums;

import BookPick.mvp.global.api.ErrorCode.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CurationErrorCode implements ErrorCodeInterface {

    CURATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 큐레이션을 찾을 수 없습니다."),
    CURATION_ACCESS_DENIED(HttpStatus.FORBIDDEN, "큐레이션 접근 권한이 없습니다.");



    private final HttpStatus status;
    private final String message;
}