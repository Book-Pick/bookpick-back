package BookPick.mvp.domain.curation.dto.create.Req;

import java.util.List;
public record RecommendDto(
    List<String> moods,
    List<String> genres,
    List<String> keywords,
    List<String> styles
) {}