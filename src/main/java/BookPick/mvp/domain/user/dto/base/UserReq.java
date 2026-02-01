package BookPick.mvp.domain.user.dto.base;

import BookPick.mvp.domain.auth.Roles;

public record UserReq(
        Long id,
        String email,
        String passWord,
        String nickName,
        String profileImage,
        String introduction,
        Roles role) {}
