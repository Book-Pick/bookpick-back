package BookPick.mvp.domain.user.enums;

import BookPick.mvp.global.enums.SuccessCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserSuccessCode implements SuccessCodeInterface {

    CREATE_USER_SUCCESS(HttpStatus.CREATED, "사용자 생성을 성공하였습니다."),
    GET_USERS_SUCCESS(HttpStatus.OK, "사용자 목록 조회를 성공하였습니다."),
    GET_MY_PROFILE_SUCCESS(HttpStatus.OK, "사용자 프로필 조회를 성공하였습니다."),
    UPDATE_MY_PROFILE_SUCCESS(HttpStatus.OK, "사용자 프로필 수정을 성공하였습니다."),
    SOFT_DELETE_MY_PROFILE_SUCCESS(HttpStatus.OK, "사용자 임시 삭제를 성공하였습니다."),
    HARD_DELETE_MY_PROFILE_SUCCESS(HttpStatus.OK, "사용자 완전 삭제를 성공하였습니다."),
    PASSWORD_CHANGE_SUCCESS(HttpStatus.OK, "비밀번호 변경을 성공하였습니다.");

    private final HttpStatus status;
    private final String message;
}
