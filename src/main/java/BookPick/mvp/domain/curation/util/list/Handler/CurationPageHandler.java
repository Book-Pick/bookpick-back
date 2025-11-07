package BookPick.mvp.domain.curation.util.list.Handler;


import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.enums.SortType;
import BookPick.mvp.domain.curation.dto.base.get.list.CurationContentRes;
import BookPick.mvp.domain.curation.dto.base.get.list.CursorPage;
import BookPick.mvp.domain.curation.model.Curation;
import BookPick.mvp.domain.curation.util.list.fetcher.CurationFetcher;
import BookPick.mvp.domain.preference.Exception.UserReadingPreferenceNotExisted;
import BookPick.mvp.domain.preference.entity.ReadingPreference;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CurationPageHandler {

    private final CurationFetcher curationFetcher;

    // 1. 데이터 조회 (size+1개 : +1을 하는 이유는 다음 것을 항상 확인하기 위해서
    public List<Curation> getCurationsPage(SortType sortType, Long cursor, int size, ReadingPreferenceInfo readingPreferenceInfo) {
        Pageable pageable = PageRequest.of(0, size + 1);
        // SORT_SIMILARITY일 경우 preferenceInfo를 사용, 나머지는 user 관련 정보 필요 없음
        if (sortType == SortType.SORT_SIMILARITY && readingPreferenceInfo == null) {
            throw new UserReadingPreferenceNotExisted();
        }


        // 1) DB에서 실제로 가져오는 로직 (fetch : DB에서 가져오는 행위)
        return curationFetcher.fetchCurations(sortType, cursor, pageable, readingPreferenceInfo);
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