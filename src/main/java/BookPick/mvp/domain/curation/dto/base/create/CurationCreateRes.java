package BookPick.mvp.domain.curation.dto.base.create;

import BookPick.mvp.domain.curation.entity.Curation;

public record CurationCreateRes(
        Long id
) {
    public static CurationCreateRes from(Curation curation){
        return new CurationCreateRes(curation.getId());
    }
}
