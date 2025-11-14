package BookPick.mvp.domain.auth.enums;

import BookPick.mvp.global.enums.ErrorCodeInterface;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCodeInterface {
     // -- Auth
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청 인자입니다."),                          // 400 회원 가입
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일 입니다."),                           // 409 회원 가입
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘 못 되었습니다."),  // 401 로그인
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");

    private final HttpStatus status;
    private final String message;


}

