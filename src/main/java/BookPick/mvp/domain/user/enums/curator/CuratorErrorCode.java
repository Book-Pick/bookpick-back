package BookPick.mvp.domain.user.enums.curator;

import BookPick.mvp.global.enums.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CuratorErrorCode implements ErrorCodeInterface {

    // -- Curator --
    Curator_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다.");                   //404

    private final HttpStatus status;
    private final String message;

}
