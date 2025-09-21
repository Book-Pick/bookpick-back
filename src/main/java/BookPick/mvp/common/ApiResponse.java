package BookPick.mvp.common;

import lombok.Getter;

// 응답 포멧
// 이 포멧 안에 data 부분에 각각의 DTO

@Getter
public class ApiResponse<T> {

    private final int status;
    private final String message;
    private final T data;   //DTO

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 정적 팩토리 메서드
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>( 200, null, data) ;
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, null, data);
    }

    public static ApiResponse<Void> noContent() {
        return new ApiResponse<>(204, null, null);
    }

    public static ApiResponse<Void> error(int status, String message) {
        return new ApiResponse<>(status, message,null);
    }
}
