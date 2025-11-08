package BookPick.mvp.domain.curation.dto.base.create.Req;

import java.util.List;
public record RecommendDto(
    List<String> moods,
    List<String> genres,
    List<String> keywords,
    List<String> styles
) {}