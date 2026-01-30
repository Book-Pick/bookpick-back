package BookPick.mvp.domain.curation.util.list.similarity;

import BookPick.mvp.domain.curation.util.gemini.dto.CurationMatchResult;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class CurationMatchResultPagination {

    /** offset 기준으로 큐레이션 리스트 페이징 처리 cursor를 offset으로 해석 (null이면 0부터 시작) */
    public static List<CurationMatchResult> paginate(
            List<CurationMatchResult> curationMatchResults, Long cursor, Pageable pageable) {
        // cursor를 offset으로 해석 (null이면 0)
        int offset = (cursor != null) ? cursor.intValue() : 0;

        // offset이 범위를 벗어나면 빈 리스트 반환
        if (offset >= curationMatchResults.size()) {
            return List.of();
        }

        int end = Math.min(offset + pageable.getPageSize(), curationMatchResults.size());
        return curationMatchResults.subList(offset, end);
    }
}
