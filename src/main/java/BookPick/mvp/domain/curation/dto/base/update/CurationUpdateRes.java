// CurationUpdateRes.java
package BookPick.mvp.domain.curation.dto.base.update;

import BookPick.mvp.domain.curation.entity.Curation;

public record CurationUpdateRes(
        Long id,
        boolean isDrafted
) {
    public static CurationUpdateRes from(Curation curation) {
        return new CurationUpdateRes(curation.getId(), curation.getIsDrafted());
    }
}