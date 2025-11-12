package BookPick.mvp.domain.user.dto.base.create;

import springboot.kakao_boot_camp.domain.user.enums.UserRole;
import springboot.kakao_boot_camp.domain.user.model.User;

public record UserCreateReq(
        Long id,
        String email,
        String passWord,
        String nickName,
        String profileImage,
        UserRole role
) {
    public UserCreateReq from(User user){
        return new UserCreateReq(
                user.getId(),
                user.getEmail(),
                user.getPassWord(),
                user.getNickName(),
                user.getProfileImage(),
                user.getRole()
        );
    }
}
