package BookPick.mvp.domain.curation.util.list.fetcher;

import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.entity.CurationLike;
import BookPick.mvp.domain.curation.enums.common.SortType;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.repository.like.CurationLikeRepository;
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
    private final CurationLikeRepository curationLikeRepository;
    private final CurationRecommendationService curationRecommendationService;


    // 1. sort Type별로 큐레이션 리스트 가져오기
    public List<Curation> fetchCurations(Long userId, SortType sortType, Long cursor, Pageable pageable, ReadingPreferenceInfo readingPreferenceInfo) {


        // 1) 맨 처음 페이지 로딩
        if (cursor == null) {
            if (sortType.equals(SortType.SORT_LATEST))
                return curationRepository.findAllByOrderByCreatedAtDesc(pageable);  // 취향 유사도 만들기 전까진 최신순
        }

        // 2) 🌟분류 기준 🌟
        return switch (sortType) {
            // 인기순
            case SORT_POPULAR -> curationRepository.findCurationsByPopularity(cursor, pageable);

            // 최신순
            case SORT_LATEST -> curationRepository.findLatestCurations(cursor, pageable);

            // 취향 유사도순
            case SORT_SIMILARITY -> {
                List<CurationMatchResult> recommended = curationRecommendationService.recommend(readingPreferenceInfo);
                List<CurationMatchResult> paginated = CurationMatchResultPagination.paginate(recommended, cursor, pageable);
                yield paginated.stream().map(CurationMatchResult::getCuration).collect(Collectors.toList());
            }

            // 좋아요 순
            case SORT_LIKED -> {
                List<CurationLike> likedCurationList = curationLikeRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
                yield likedCurationList.stream()
                        .map(CurationLike::getCuration)
                        .toList();
            }

            // 내가 작성한 순
            case SORT_MY -> curationRepository.findByUserId(userId, pageable);
        };
    }

    public Long calculateNextCursor(List<Curation> curations, int size, boolean hasNext) {
        return hasNext ? curations.get(size).getId() : null;
    }
}