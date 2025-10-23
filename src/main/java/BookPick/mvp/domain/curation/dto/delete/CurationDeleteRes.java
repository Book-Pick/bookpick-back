// CurationDeleteRes.java
package BookPick.mvp.domain.curation.dto.delete;

import java.time.LocalDateTime;

public record CurationDeleteRes(
        Long id,
        LocalDateTime deletedAt
) {
    public static CurationDeleteRes from(Long id, LocalDateTime deletedAt) {
        return new CurationDeleteRes(id, deletedAt);
    }
}