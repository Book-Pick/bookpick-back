package BookPick.mvp.domain.user.dto.base.delete;

import BookPick.mvp.domain.user.entity.User;
import java.time.LocalDateTime;

public record UserSoftDeleteRes(Long userId, Boolean deleted, LocalDateTime deletedAt) {
    public static UserSoftDeleteRes from(User user) {
        return new UserSoftDeleteRes(user.getId(), user.isDeleted(), user.getDeletedAt());
    }
}
