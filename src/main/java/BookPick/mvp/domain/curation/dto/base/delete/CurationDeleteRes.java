// CurationDeleteRes.java
package BookPick.mvp.domain.curation.dto.base.delete;

import java.time.LocalDateTime;

public record CurationDeleteRes(
        Long curationIds,
        LocalDateTime deletedAt
) {
    public static CurationDeleteRes from(Long id, LocalDateTime deletedAt) {
        return new CurationDeleteRes(id, deletedAt);
    }
}