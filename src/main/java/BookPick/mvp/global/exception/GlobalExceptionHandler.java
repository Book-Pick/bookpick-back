package BookPick.mvp.global.exception;


import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())   // DTO의 @Email(message="...") 내용
                .orElse("잘못된 요청입니다.");

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())   // 400
                .body(ApiResponse.customError(ErrorCode.INVALID_REQUEST.getStatus(), errorMessage, null));
    }



}
