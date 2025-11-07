// CurationUpdateRes.java
package BookPick.mvp.domain.curation.dto.base.update;

import BookPick.mvp.domain.curation.model.Curation;

public record CurationUpdateRes(
        Long id
) {
    public static CurationUpdateRes from(Curation curation) {
        return new CurationUpdateRes(curation.getId());
    }
}