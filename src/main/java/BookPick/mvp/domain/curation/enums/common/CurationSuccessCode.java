package BookPick.mvp.domain.curation.enums.common;

import BookPick.mvp.global.enums.SuccessCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CurationSuccessCode implements SuccessCodeInterface {


    // 1. 임시저장
    CREATE_DRAFTED_CURATION_SUCCESS(HttpStatus.CREATED, "큐레이션 임시저장에 성공했습니다"),

    // 2. 좋아요
    CURATION_LIKE_SUCCESS(HttpStatus.OK, "큐레이션 좋아요를 성공적으로 실행하였습니다."),
    CURATION_DISLIKE_SUCCESS(HttpStatus.OK, "큐레이션 좋아요 취소를 성공적으로 실행하였습니다.");





    private final HttpStatus status;
    private final String message;
}
