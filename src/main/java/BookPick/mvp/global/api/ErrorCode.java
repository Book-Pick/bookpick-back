package BookPick.mvp.global.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // -- Auth
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),                          // 400 회원 가입
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다."),                           // 409 회원 가입
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘 못 되었습니다."),  // 401 로그인

    Token_Expired(HttpStatus.UNAUTHORIZED, "로그인 토큰이 만료되었습니다. 다시 로그인해주세요."),
    Invalid_Token_Type(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 형식입니다. 다시 로그인해주세요."),


    // -- User --
    User_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),                    //404


    // -- Post --
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 큐레이션을 찾을 수 없습니다."),                    //404


    // --  Comment --
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글 찾을 수 없습니다."),                     //404

    // -- Reading Preference --
    READING_PREFERENCE_ALREADY_RESiGSTER(HttpStatus.CONFLICT, "이미 독서 취향이 존재합니다."),
    READING_PREFERENCE_NOT_EXISTED(HttpStatus.NOT_FOUND, "사용자의 독서 취향이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}

