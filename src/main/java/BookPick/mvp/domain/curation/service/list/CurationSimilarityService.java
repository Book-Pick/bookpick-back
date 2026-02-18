package BookPick.mvp.domain.curation.service.list;

import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.util.list.similarity.SimilarityMatcher;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 순수 매칭 기반 큐레이션 유사도 서비스
 * Gemini AI 없이 사용자 취향과 큐레이션 태그를 직접 비교
 */
@Service
@RequiredArgsConstructor
public class CurationSimilarityService {

    private final CurationRepository curationRepository;

    /**
     * 사용자 취향 기반 큐레이션 추천 (순수 매칭)
     *
     * @param preferenceInfo 사용자 독서 취향
     * @param userId 현재 사용자 ID (본인 큐레이션 제외용)
     * @return 유사도 점수 내림차순 정렬된 큐레이션 + 점수 리스트
     */
    @Transactional(readOnly = true)
    public List<CurationWithScore> getRecommendedCurations(
            ReadingPreferenceInfo preferenceInfo, Long userId) {

        // 1. 발행된 큐레이션 조회 (본인 제외, 삭제되지 않은 것)
        List<Curation> curations = curationRepository.findAllPublishedExcludeUser(userId);

        // 2. 각 큐레이션에 대해 유사도 점수 계산 및 정렬
        return curations.stream()
                .map(curation -> new CurationWithScore(
                        curation,
                        SimilarityMatcher.calculate(curation, preferenceInfo),
                        SimilarityMatcher.getMatchedSummary(curation, preferenceInfo)))
                .sorted(Comparator.comparingInt(CurationWithScore::score).reversed())
                .toList();
    }

    /**
     * 큐레이션 + 유사도 점수 래퍼
     */
    public record CurationWithScore(
            Curation curation,
            int score,
            String matched) {}
}
