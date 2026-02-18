package BookPick.mvp.domain.user.dto.subscribe;

import BookPick.mvp.domain.user.entity.User;
import lombok.Builder;

@Builder
public record SubscribedCuratorRes(
        Long curatorId, String nickname, String profileImageUrl, String bio) {
    public static SubscribedCuratorRes from(User curator) {
        return SubscribedCuratorRes.builder()
                .curatorId(curator.getId())
                .nickname(curator.getNickname())
                .profileImageUrl(curator.getProfileImageUrl())
                .bio(curator.getBio())
                .build();
    }
}
