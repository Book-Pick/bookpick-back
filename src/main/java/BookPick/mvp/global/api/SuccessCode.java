package BookPick.mvp.global.api;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {

    // -- Auth --
    REGISTER_SUCCESS(HttpStatus.CREATED, "REGISTER_SUCCESS", "회원가입을 성공하였습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "LOGIN_SUCCESS", "로그인에 성공했습니다"),

    // -- User --

    // -- Post --
    POST_CREATE_SUCCESS(HttpStatus.CREATED, "POST_CREATE_SUCCESS", "게시글을 성공적으로 등록하였습니다."),
    POST_READ_SUCCESS(HttpStatus.OK, "POST_READ_SUCCESS", "게시글 조회를 성공하였습니다."),
    POST_UPDATE_SUCCESS(HttpStatus.OK, "POST_UPDATE_SUCCESS", "게시글을 성공적으로 수정하였습니다."),
    POST_DELETE_SUCCESS(HttpStatus.OK, "POST_DELETE_SUCCESS", "게시글을 성공적으로 삭제하였습니다."),

    // -- Pre
    PREFERENCE_CREATED(HttpStatus.CREATED, "PREFERENCE_CREATED", "사용자의 독서 취향 정보를 성공적으로 등록하였습니다."),
    PREFERENCE_READ_SUCCESS(HttpStatus.OK, "PREFERENCE_READ_SUCCESS", "사용자의 독서 취향 정보를 성공적으로 조회하였습니다."),
    PREFERENCE_UPDATED(HttpStatus.OK, "PREFERENCE_UPDATED", "사용자의 독서 취향 정보를 성공적으로 수정하였습니다."),
    PREFERENCE_DELETED(HttpStatus.OK, "PREFERENCE_DELETED", "사용자의 독서 취향 정보를 성공적으로 삭제하였습니다.");




    private final HttpStatus status;    // 상태 코드 번호
    private final String code;          // 상태 설명 (개발용)
    private final String message;       // 상태 설명 (사용자 친화적)




}
