package BookPick.mvp.domain.curation.dto.base.get.one;

import java.util.List;

public record RecommendInfo(List<String> moods, List<String> genres,
                            List<String> keywords, List<String> styles) {
}
