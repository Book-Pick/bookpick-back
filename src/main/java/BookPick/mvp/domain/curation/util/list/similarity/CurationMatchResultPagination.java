package BookPick.mvp.domain.curation.util.list.similarity;

import BookPick.mvp.domain.curation.util.gemini.dto.CurationMatchResult;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CurationMatchResultPagination {

    /**
     * cursor 기준으로 큐레이션 리스트 페이징 처리
     */
    public static List<CurationMatchResult> paginate(List<CurationMatchResult> curationMatchResults, Long cursor, Pageable pageable) {
        int start = 0;

        if (cursor != null) {
            for (int i = 0; i < curationMatchResults.size(); i++) {
                if (curationMatchResults.get(i).getCuration().getId().equals(cursor)) {
                    start = i + 1;
                    break;
                }
            }
        }

        int end = Math.min(start + pageable.getPageSize(), curationMatchResults.size());
        return curationMatchResults.subList(start, end);
    }
}
