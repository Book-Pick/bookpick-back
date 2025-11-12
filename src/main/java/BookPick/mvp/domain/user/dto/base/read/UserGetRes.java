package BookPick.mvp.domain.user.dto.base.read;

import springboot.kakao_boot_camp.domain.user.enums.UserRole;
import springboot.kakao_boot_camp.domain.user.model.User;

import java.time.LocalDateTime;

public record UserGetRes(
        Long userId,
        String email,
        String nickName,
        String profileImage,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean deleted
) {
    public static UserGetRes from(User user) {
        return new UserGetRes(
                user.getId(),
                user.getEmail(),
                user.getNickName(),
                user.getProfileImage(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt(),
                user.isDeleted()
        );
    }
}
