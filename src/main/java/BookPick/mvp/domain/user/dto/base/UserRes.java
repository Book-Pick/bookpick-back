package BookPick.mvp.domain.user.dto.base;

import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.user.entity.User;
import java.time.LocalDateTime;

public record UserRes(
        Long userId,
        String email,
        String nickName,
        String profileImage,
        String introduction,
        Roles role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean deleted) {
    public static UserRes from(User user) {
        return new UserRes(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImageUrl(),
                user.getBio(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt(),
                user.isDeleted());
    }
}
