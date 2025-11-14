package BookPick.mvp.domain.user.dto.base;

import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.user.entity.User;

public record UserReq(
        Long id,
        String email,
        String passWord,
        String nickName,
        String profileImage,
        Roles role
) {
    public UserReq from(User user){
        return new UserReq(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getProfileImageUrl(),
                user.getRole()
        );
    }
}
