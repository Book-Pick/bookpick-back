package BookPick.mvp.domain.curation.service.list;

import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;
import BookPick.mvp.domain.ReadingPreference.repository.ReadingPreferenceRepository;
import BookPick.mvp.domain.curation.dto.base.get.list.CurationContentRes;
import BookPick.mvp.domain.curation.dto.base.get.list.CurationListGetRes;
import BookPick.mvp.domain.curation.dto.base.get.list.CursorPage;
import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.enums.common.SortType;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.repository.like.CurationLikeRepository;
import BookPick.mvp.domain.curation.util.list.Handler.CurationPageHandler;
import BookPick.mvp.domain.curation.util.list.similarity.SimilarityMatcher;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurationListService {

    private final CurationPageHandler pageHandler;
    private final ReadingPreferenceRepository readingPreferenceRepository;
    private final CurationSimilarityService curationSimilarityService;
    private final CurationLikeRepository curationLikeRepository;
    private final CurationRepository curationRepository;

    // 1. 큐레이션 리스트 조회
    public CurationListGetRes getCurations(
            SortType sortType, Long cursor, int size, boolean drafted, Long userId) {

        // 1. 내 취향 유사도 순 (순수 매칭 - Gemini 미사용)
        if (sortType == SortType.SORT_SIMILARITY) {

            // 1. 유저 독서 취향 반환
            ReadingPreference readingPreference =
                    readingPreferenceRepository.findByUserId(userId).orElse(null);
            if (readingPreference == null || !readingPreference.isCompleted()) {
                return CurationListGetRes.ofEmpty(sortType);
            }
            ReadingPreferenceInfo preferenceInfo = ReadingPreferenceInfo.from(readingPreference);

            // 2. 순수 매칭으로 큐레이션 조회 (유사도 점수 기준 정렬됨)
            List<CurationSimilarityService.CurationWithScore> allResults =
                    curationSimilarityService.getRecommendedCurations(preferenceInfo, userId);

            // 3. 페이지네이션 (cursor를 offset으로 사용)
            int offset = (cursor != null) ? cursor.intValue() : 0;
            int endIndex = Math.min(offset + size + 1, allResults.size());
            List<CurationSimilarityService.CurationWithScore> paginated =
                    (offset < allResults.size())
                            ? allResults.subList(offset, endIndex)
                            : List.of();

            // 4. 다음 페이지 존재 여부
            boolean hasNext = paginated.size() > size;

            // 5. 실제 반환할 컨텐츠
            List<CurationSimilarityService.CurationWithScore> contentResults =
                    hasNext ? paginated.subList(0, size) : paginated;

            // 6. 다음 커서
            Long nextCursor = hasNext ? (long) (offset + size) : null;

            // 7-1. 좋아요 여부 계산을 위한 큐레이션 ID 목록 추출
            List<Long> curationIds =
                    contentResults.stream().map(r -> r.curation().getId()).toList();

            // 7-2. 사용자가 좋아요 누른 큐레이션 ID 조회
            Set<Long> likedIds =
                    curationLikeRepository.findLikedCurationIds(userId, curationIds).stream()
                            .collect(Collectors.toSet());

            // 8. 큐레이션 리스트 Dto 생성 (순수 매칭 점수 사용)
            List<CurationContentRes> content =
                    contentResults.stream()
                            .map(result -> CurationContentRes.fromWithScore(
                                    result.curation(),
                                    result.score(),
                                    result.matched(),
                                    likedIds.contains(result.curation().getId())))
                            .collect(Collectors.toList());

            // 9. 큐레이션 리스트로 감싸기
            return CurationListGetRes.from(sortType, content, hasNext, nextCursor);
        }

        // 2) 내 취향 유사도순이 아닌 경우
        List<Curation> curations =
                pageHandler.getCurationsPage(userId, sortType, cursor, size, null, drafted);
        CursorPage<Curation> page = pageHandler.createCursorPage(curations, size);

        // 2-1. 좋아요 여부 계산을 위한 큐레이션 ID 목록 추출
        List<Long> curationIds = page.getContent().stream().map(Curation::getId).toList();

        // 2-2. 사용자가 좋아요 누른 큐레이션 ID 조회
        Set<Long> likedIds =
                curationLikeRepository.findLikedCurationIds(userId, curationIds).stream()
                        .collect(Collectors.toSet());

        List<CurationContentRes> content =
                page.getContent().stream()
                        .map(c -> CurationContentRes.from(c, likedIds.contains(c.getId())))
                        .collect(Collectors.toList());

        return CurationListGetRes.from(sortType, content, page.isHasNext(), page.getNextCursor());
    }

    // Issue 1) DTO 만들어서 독서취향 정보 레이어간 소통 vs 사용자 독서취향 실시간 수정 반영 고려
    // 1. 사용자는 독서취향을 한번 설정하면 자주 바꾸지 않는다.
    // 2. 따라서 DTO 생성 후 넣기로 결정

    // 2. curationId 목록으로 큐레이션 조회
    @Transactional(readOnly = true)
    public List<CurationContentRes> getCurationsByIds(List<Long> curationIds, Long userId) {
        List<Curation> curations = curationRepository.findByIdIn(curationIds);

        Set<Long> likedIds =
                curationLikeRepository.findLikedCurationIds(userId, curationIds).stream()
                        .collect(Collectors.toSet());

        return curations.stream()
                .map(c -> CurationContentRes.from(c, likedIds.contains(c.getId())))
                .collect(Collectors.toList());
    }
}
