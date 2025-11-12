package BookPick.mvp.domain.user.dto.base.create;

import springboot.kakao_boot_camp.domain.user.model.User;

public record UserCreateRes(
    Long id
) {
    public static UserCreateRes from(User user){
        return new UserCreateRes(user.getId());
    }
    }
