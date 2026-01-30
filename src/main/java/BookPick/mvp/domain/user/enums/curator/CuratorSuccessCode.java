package BookPick.mvp.domain.user.enums.curator;

import BookPick.mvp.global.enums.SuccessCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CuratorSuccessCode implements SuccessCodeInterface {
    CURATOR_SUBSCRIBE_SUCCESS(HttpStatus.CREATED, "큐레이션 구독을 성공적으로 실행하였습니다."),
    CURATOR_SUBSCRIBE_CANCLE_SUCCESS(HttpStatus.OK, "큐레이션 구독 취소를 성공적으로 실행하였습니다."),

    GET_CURATOR_SUBSCRIBE_LIST_SUCCESS(HttpStatus.OK, "큐레이터 구독 리스트를 성공적으로 조회하였습니다.");

    private final HttpStatus status;
    private final String message;
}
