// CurationUpdateRes.java
package BookPick.mvp.domain.curation.dto.update;

import BookPick.mvp.domain.curation.entity.Curation;

public record CurationUpdateRes(
        Long id
) {
    public static CurationUpdateRes from(Curation curation) {
        return new CurationUpdateRes(curation.getId());
    }
}