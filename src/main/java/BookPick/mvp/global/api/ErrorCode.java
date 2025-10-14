package BookPick.mvp.global.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // -- Auth
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),                          // 400 회원 가입
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일 입니다."),                           // 409 회원 가입
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘 못 되었습니다."),  // 401 로그인

    // -- User --
    User_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),                    //404


    // -- Post --
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 큐레이션을 찾을 수 없습니다."),                    //404


    // --  Comment --
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글 찾을 수 없습니다.");                     //404


    private final HttpStatus status;
    private final String message;
}

