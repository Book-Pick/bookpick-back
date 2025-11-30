package BookPick.mvp.domain.user.enums.curator;

import BookPick.mvp.global.enums.SuccessCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CuratorSuccessCode implements SuccessCodeInterface {


    PASSWORD_CHANGE_SUCCESS(HttpStatus.OK, "비밀번호 변경을 성공하였습니다.");

    private final HttpStatus status;
    private final String message;
}
