package BookPick.mvp.domain.curation.dto.base.get.list;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CurationIdsReq(@NotEmpty(message = "curationIds는 필수입니다.") List<Long> curationIds) {}
