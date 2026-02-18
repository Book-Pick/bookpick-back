package BookPick.mvp.global.api.SuccessCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {

    // -- Auth --
    REGISTER_SUCCESS(HttpStatus.OK, "회원가입을 성공하였습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다"),
    KAKAO_LOGIN_SUCCESS(HttpStatus.OK, "카카오 로그인에 성공했습니다"),
    TOKEN_REFERSH_SUCCESS(HttpStatus.OK, "액세스 토큰 재발급에 성공하였습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공했습니다"),

    // -- User --
    GET_USERS_SUCCESS(HttpStatus.OK, "사용자 목록 조회를 성공하였습니다."),

    // -- Comment --
    COMMENT_CREATE_SUCCESS(HttpStatus.CREATED, "댓글을 성공적으로 등록하였습니다."),
    COMMENT_LIST_READ_SUCCESS(HttpStatus.OK, "댓글 목록을 성공적으로 조회하였습니다."),
    COMMENT_LIST_EMPTY(HttpStatus.OK, "댓글이 없습니다."),
    COMMENT_READ_SUCCESS(HttpStatus.OK, "댓글을 성공적으로 조회하였습니다."),
    COMMENT_UPDATE_SUCCESS(HttpStatus.OK, "댓글을 성공적으로 수정하였습니다."),
    COMMENT_DELETE_SUCCESS(HttpStatus.OK, "댓글을 성공적으로 삭제하였습니다."),

    // -- Book --
    BOOK_LIST_READ_SUCCESS(HttpStatus.OK, "책 목록을 성공적으로 조회하였습니다."),
    BOOK_LINK_READ_SUCCESS(HttpStatus.OK, "책 구매 링크를 성공적으로 조회하였습니다."),
    READING_PREFERENCE_REGISTER_SUCCESS(HttpStatus.CREATED, "독서 취향을 성공적으로 등록하였습니다."),

    // -- Reading Preference --
    READING_PREFERENCE_READ_SUCCESS(HttpStatus.CREATED, "독서 취향을 성공적으로 조회하였습니다."),
    READING_PREFERENCE_UPDATE_SUCCESS(HttpStatus.CREATED, "독서 취향을 성공적으로 수정하였습니다."),
    READING_PREFERENCE_DELETE_SUCCESS(HttpStatus.CREATED, "독서 취향을 성공적으로 삭제하였습니다."),

    // -- Curation --
    CURATION_PUBLISH_SUCCESS(HttpStatus.CREATED, "큐레이션을 성공적으로 발행하였습니다."),
    DRAFTED_CURATION_PUBLISH_SUCCESS(HttpStatus.OK, "임시저장 된 큐레이션을 성공적으로 발행하였습니다."),
    CURATION_DRAFT_SUCCESS(HttpStatus.CREATED, "큐레이션을 성공적으로 임시저장하였습니다."),
    CURATION_DRAFT_UPDATE_SUCCESS(HttpStatus.OK, "큐레이션 임시저장을 성공적으로 수정하였습니다."),
    CURATION_GET_SUCCESS(HttpStatus.OK, "큐레이션을 성공적으로 단건 조회하였습니다."),
    CURATION_LIST_GET_SUCCESS(HttpStatus.OK, "큐레이션을 성공적으로 리스트 조회하였습니다."),
    CURATION_UPDATE_SUCCESS(HttpStatus.OK, "발행된 큐레이션을 성공적으로 수정하였습니다.");

    private final HttpStatus status;
    private final String message;
}
