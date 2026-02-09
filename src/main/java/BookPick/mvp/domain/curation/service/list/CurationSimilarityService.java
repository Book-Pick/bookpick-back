package BookPick.mvp.domain.curation.service.list;

import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.util.list.similarity.SimilarityMatcher;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    // 사전 필터링 후 최대 후보 수 (인기순 상위 N개)
    private static final int MAX_CANDIDATES = 200;

    // JPA IN절 빈 리스트 방지용 sentinel 값
    private static final String EMPTY_SENTINEL = "__NONE__";

    /**
     * 사용자 취향 기반 큐레이션 추천 (DB 사전 필터링 + 후보 제한)
     *
     * 1단계: DB에서 태그가 하나라도 겹치는 큐레이션만 인기순 상위 200개 조회
     * 2단계: 메모리에서 유사도 점수 계산 및 정렬
     *
     * @param preferenceInfo 사용자 독서 취향
     * @param userId 현재 사용자 ID (본인 큐레이션 제외용)
     * @return 유사도 점수 내림차순 정렬된 큐레이션 + 점수 리스트
     */
    @Transactional(readOnly = true)
    public List<CurationWithScore> getRecommendedCurations(
            ReadingPreferenceInfo preferenceInfo, Long userId) {

        // 1. DB 사전 필터링: 태그 매칭되는 큐레이션만 인기순 상위 MAX_CANDIDATES개 조회
        List<Curation> curations = curationRepository.findPublishedCurationsByRecommendation(
                userId,
                safeList(preferenceInfo.moods()),
                safeList(preferenceInfo.genres()),
                safeList(preferenceInfo.keywords()),
                safeList(preferenceInfo.readingStyles()),
                PageRequest.of(0, MAX_CANDIDATES));

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
     * 빈 리스트를 sentinel 값으로 대체 (JPA IN절 빈 리스트 에러 방지)
     */
    private List<String> safeList(List<String> list) {
        return (list == null || list.isEmpty()) ? List.of(EMPTY_SENTINEL) : list;
    }

    /**
     * 큐레이션 + 유사도 점수 래퍼
     */
    public record CurationWithScore(
            Curation curation,
            int score,
            String matched) {}
}
