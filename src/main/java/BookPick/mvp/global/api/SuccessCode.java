package BookPick.mvp.global.api;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum SuccessCode {

    // -- Auth --
    REGISTER_SUCCESS(HttpStatus.OK, "회원가입을 성공하였습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다"),

    // -- User --
    GET_USERS_SUCCESS(HttpStatus.OK, "사용자 목록 조회를 성공하였습니다."),

    // -- Quration --
    CURATION_CREATE_SUCCESS(HttpStatus.CREATED, "큐레이션을 성공적으로 등록하였습니다."),
    CURATION_LIST_READ_SUCCESS(HttpStatus.OK, "큐레이션 목록을 성공적으로 조회하였습니다."),
    CURATION_DETAIL_READ_SUCCESS(HttpStatus.OK, "큐레이션을 성공하였습니다."),
    CURATION_UPDATE_SUCCESS(HttpStatus.OK, "큐레이션을 성공적으로 수정하였습니다."),
    CURATION_DELETE_SUCCESS(HttpStatus.OK, "큐레이션을 성공적으로 삭제하였습니다."),


    // -- Comment --
    COMMENT_CREATE_SUCCESS(HttpStatus.CREATED, "댓글을 성공적으로 등록하였습니다."),
    COMMENT_LIST_READ_SUCCESS(HttpStatus.OK, "댓글 목록을 성공적으로 조회하였습니다."),
    COMMENT_LIST_EMPTY(HttpStatus.OK, "댓글이 없습니다."),
    COMMENT_READ_SUCCESS(HttpStatus.OK, "댓글을 성공적으로 조회하였습니다."),
    COMMENT_UPDATE_SUCCESS(HttpStatus.OK, "댓글을 성공적으로 수정하였습니다."),
    COMMENT_DELETE_SUCCESS(HttpStatus.OK, "댓글을 성공적으로 삭제하였습니다."),

    // -- Book --
    BOOK_LIST_READ_SUCCESS(HttpStatus.OK, "책 목록을 성공적으로 조회하였습니다."),

    // -- Reading Preference --
    READING_PREFERENCE_REGISTER_SUCCESS(HttpStatus.CREATED, "독서 취향을 성공적으로 등록하였습니다."),
    READING_PREFERENCE_READ_SUCCESS(HttpStatus.CREATED, "독서 취향을 성공적으로 조회하였습니다."),
    READING_PREFERENCE_UPDATE_SUCCESS(HttpStatus.CREATED, "독서 취향을 성공적으로 수정하였습니다."),
    READING_PREFERENCE_DELETE_SUCCESS(HttpStatus.CREATED, "독서 취향을 성공적으로 삭제하였습니다.");



    private final HttpStatus status;
    private final String message;       // 상태 설명 (사용자 친화적)
}





