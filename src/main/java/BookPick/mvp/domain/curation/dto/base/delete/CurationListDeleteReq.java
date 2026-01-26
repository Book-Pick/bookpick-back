// CurationDeleteRes.java
package BookPick.mvp.domain.curation.dto.base.delete;

import java.time.LocalDateTime;
import java.util.List;

public record CurationListDeleteReq(
        List<Long> curationIds
) {
}