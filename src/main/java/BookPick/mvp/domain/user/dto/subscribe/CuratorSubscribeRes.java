package BookPick.mvp.domain.user.dto.subscribe;

import BookPick.mvp.domain.user.entity.CuratorSubscribe;
import BookPick.mvp.domain.user.entity.User;

public record CuratorSubscribeRes(
         Long curatorId,
         boolean subscribed
){


    public static CuratorSubscribeRes from(User curator, boolean subscribed){
        return new CuratorSubscribeRes(curator.getId(),subscribed);
    }

    public static CuratorSubscribeRes from(CuratorSubscribe curationSubscribe, boolean subscribed){
        return new CuratorSubscribeRes(curationSubscribe.getCurator().getId(),subscribed);
    }
}
