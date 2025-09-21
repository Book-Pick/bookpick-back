package BookPick.mvp.global.exception;

import BookPick.mvp.common.ApiResponse;
import BookPick.mvp.domain.user.exception.DuplicateEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // @Valid 실패 → 400
public ResponseEntity<ApiResponse<Void>> handleBadRequest(MethodArgumentNotValidException ex) {
    return ResponseEntity.badRequest()
            .body(ApiResponse.error(400, "invalid_request"));
}

@ExceptionHandler(DuplicateEmailException.class) // 이메일 중복 → 409
public ResponseEntity<ApiResponse<Void>> handleConflict(DuplicateEmailException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ApiResponse.error(409, "duplicate_email"));
}

@ExceptionHandler(Exception.class) // 그 외 모든 예외 → 500
public ResponseEntity<ApiResponse<Void>> handle500(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(500, "server_error"));
}

}

