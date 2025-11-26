package BookPick.mvp.domain.curation.dto.subscribe;

import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.entity.CurationSubscribe;

public record CurationSubscribeDto(
         Long curationId,
         boolean subscribed
){
    public static CurationSubscribeDto from(Curation curation, boolean subscribed){
        return new CurationSubscribeDto(curation.getId(),subscribed);
    }

    public static CurationSubscribeDto from(CurationSubscribe curationSubscribe, boolean subscribed){
        return new CurationSubscribeDto(curationSubscribe.getCuration().getId(),subscribed);
    }
}
