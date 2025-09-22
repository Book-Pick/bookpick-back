package BookPick.mvp.domain.user.exception;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException() {
        super("잘못된 로그인 시도입니다.");
    }

    public InvalidLoginException(String message) {
        super(message);
    }
}
