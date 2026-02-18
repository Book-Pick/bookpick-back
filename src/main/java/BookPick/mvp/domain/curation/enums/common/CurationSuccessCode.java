package BookPick.mvp.domain.curation.enums.common;

import BookPick.mvp.global.enums.SuccessCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CurationSuccessCode implements SuccessCodeInterface {

    // 임시저장
    CREATE_DRAFTED_CURATION_SUCCESS(HttpStatus.CREATED, "큐레이션 임시저장에 성공했습니다"),

    // 좋아요
    CURATION_LIKE_SUCCESS(HttpStatus.OK, "큐레이션 좋아요를 성공적으로 실행하였습니다."),
    CURATION_DISLIKE_SUCCESS(HttpStatus.OK, "큐레이션 좋아요 취소를 성공적으로 실행하였습니다."),

    // 삭제
    CURATION_DELETE_SUCCESS(HttpStatus.OK, "큐레이션을 성공적으로 삭제하였습니다."),
    CURATION_LIST_DELETE_SUCCESS(HttpStatus.OK, "다수의 큐레이션을 성공적으로 삭제하였습니다.");

    // 조회

    private final HttpStatus status;
    private final String message;
}
