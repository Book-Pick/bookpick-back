package BookPick.mvp.domain.curation.util.list.fetcher;

import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.enums.SortType;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.service.list.CurationRecommendationService;
import BookPick.mvp.domain.curation.util.gemini.dto.CurationMatchResult;
import BookPick.mvp.domain.curation.util.list.similarity.CurationMatchResultPagination;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class CurationFetcher {

    private final CurationRepository curationRepository;
    private final CurationRecommendationService curationRecommendationService;


    // 1. sort Type별로 큐레이션 리스트 가져오기
    public List<Curation> fetchCurations(SortType sortType, Long cursor, Pageable pageable, ReadingPreferenceInfo readingPreferenceInfo) {


        // 1) 맨 처음 페이지 로딩
        if (cursor == null) {
            if (sortType.equals(SortType.SORT_LATEST))
                return curationRepository.findAllByOrderByCreatedAtDesc(pageable);  // 취향 유사도 만들기 전까진 최신순
        }

        // 2) 분류 기준에 맞게 데이터 가져오기
        return switch (sortType) {
            case SORT_POPULAR -> curationRepository.findCurationsByPopularity(cursor, pageable); // 인기순
            case SORT_LATEST -> curationRepository.findLatestCurations(cursor, pageable);        // 최신순

            // 1. 큐레이션 리스트 뽑기
            // 2. 입력값 :
            case SORT_SIMILARITY -> {
                List<CurationMatchResult> recommended = curationRecommendationService.recommend(readingPreferenceInfo);
                List<CurationMatchResult> paginated = CurationMatchResultPagination.paginate(recommended, cursor, pageable);
                yield paginated.stream().map(CurationMatchResult::getCuration).collect(Collectors.toList());
            }

        };
    }

    public Long calculateNextCursor(List<Curation> curations, int size, boolean hasNext) {
        return hasNext ? curations.get(size).getId() : null;
    }
}