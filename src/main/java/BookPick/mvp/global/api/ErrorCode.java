package BookPick.mvp.global.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {


    // -- Auth --
    INVALID_REQUEST(400, "INVALID_REQUEST", "잘못된 요청입니다."),   // 400
    AUTHENTICATION_FAILED(401, "AUTHENTICATION_FAILED", "아이디 또는 비밀번호가 올바르지 않습니다."),  // 401 로그인
    DUPLICATE_EMAIL(409, "DUPLICATE_EMAIL", "중복된 이메일 입니다."), // 409 회원가입

    // -- User --

    // -- Post --
    POST_NOT_FOUND(404, "POST_NOT_FOUND", "해당 게시글을 찾을 수 없습니다.");


    private final int status;
    private final String code;
    private final String message;


}
