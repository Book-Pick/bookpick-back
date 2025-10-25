package BookPick.mvp.domain.curation.service.Handler;


import BookPick.mvp.domain.curation.SortType;
import BookPick.mvp.domain.curation.dto.get.list.CurationContentRes;
import BookPick.mvp.domain.curation.dto.get.list.CursorPage;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.service.fetcher.CurationFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CurationPageHandler {

    private final CurationFetcher curationFetcher;

    // 1. 데이터 조회 (size+1개)
    public List<Curation> fetchCurationsWithExtra(SortType sortType, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size + 1);
        return curationFetcher.fetchCurations(sortType, cursor, pageable);
    }

    // 2. 커서 페이징 처리
    public CursorPage<Curation> createCursorPage(List<Curation> curations, int size) {
        boolean hasNext = curations.size() > size;
        Long nextCursor = curationFetcher.calculateNextCursor(curations, size, hasNext);
        List<Curation> content = hasNext ? curations.subList(0, size) : curations;

        return new CursorPage<>(content, hasNext, nextCursor);
    }

    // 3. DTO 변환
    public List<CurationContentRes> convertToContentRes(List<Curation> curations) {
        return curations.stream()
                .map(CurationContentRes::from)
                .toList();
    }
}