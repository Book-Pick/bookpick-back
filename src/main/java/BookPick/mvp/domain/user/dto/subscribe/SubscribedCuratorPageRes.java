package BookPick.mvp.domain.user.dto.subscribe;

import BookPick.mvp.global.dto.PageInfo;
import lombok.Builder;

import java.util.List;

@Builder
public record SubscribedCuratorPageRes(
    List<SubscribedCuratorRes> curators,
    PageInfo pageInfo
) {
    public static SubscribedCuratorPageRes of(List<SubscribedCuratorRes> curators, PageInfo pageInfo) {
        return SubscribedCuratorPageRes.builder()
            .curators(curators)
            .pageInfo(pageInfo)
            .build();
    }
}
