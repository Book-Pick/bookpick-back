package BookPick.mvp.domain.curation.dto.create;

import BookPick.mvp.domain.curation.model.Curation;

public record CurationCreateRes(
        Long id
) {
    public static CurationCreateRes from(Curation curation){
        return new CurationCreateRes(curation.getId());
    }
}
