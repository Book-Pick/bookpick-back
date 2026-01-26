// CurationDeleteRes.java
package BookPick.mvp.domain.curation.dto.base.delete;

import java.time.LocalDateTime;
import java.util.List;

public record CurationListDeleteRes(
        List<Long> ids,
        LocalDateTime deletedAt
) {
    public static CurationListDeleteRes from(List<Long> ids, LocalDateTime deletedAt) {
        return new CurationListDeleteRes(ids, deletedAt);
    }
}