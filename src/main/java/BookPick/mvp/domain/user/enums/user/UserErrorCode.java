package BookPick.mvp.domain.user.enums.user;

import BookPick.mvp.global.enums.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCodeInterface {

     // -- User --
    User_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),                    //404
    NOT_HAVE_ADMIN_ROLE(HttpStatus.BAD_REQUEST, "관리자 권한이 없는 유저입니다."),
    ALREADY_DELETE_USR(HttpStatus.CONFLICT, "이미 삭제한 유저입니다."),                      //409, 이미 삭제한 유저기때문에 conflict 충돌이라는 의미
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "변경할 비밀번호와 확인 비밀번호가 일치하지 않습니다."),                      //409, 이미 삭제한 유저기때문에 conflict 충돌이라는 의미
    WRONG_CURRENT_PASSWORD(HttpStatus.UNAUTHORIZED, "현재 비밀번호가 올바르지 않습니다.");

    private final HttpStatus status;
    private final String message;

}
