package BookPick.mvp.domain.curation.service.list;

import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;
import BookPick.mvp.domain.curation.dto.base.get.list.CurationContentRes;
import BookPick.mvp.domain.curation.dto.base.get.list.CurationListGetRes;
import BookPick.mvp.domain.curation.dto.base.get.list.CursorPage;
import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.enums.common.SortType;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.repository.like.CurationLikeRepository;
import BookPick.mvp.domain.curation.util.gemini.dto.CurationMatchResult;
import BookPick.mvp.domain.curation.util.list.Handler.CurationPageHandler;
import BookPick.mvp.domain.curation.util.list.similarity.CurationMatchResultPagination;
import BookPick.mvp.domain.ReadingPreference.Exception.UserReadingPreferenceNotExisted;
import BookPick.mvp.domain.ReadingPreference.repository.ReadingPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurationListService {

    private final CurationPageHandler pageHandler;
    private final ReadingPreferenceRepository readingPreferenceRepository;
    private final CurationRecommendationService curationRecommendationService;
    private final CurationLikeRepository curationLikeRepository;


    // 1. 큐레이션 리스트 조회
    public CurationListGetRes getCurations(SortType sortType, Long cursor, int size, Long userId) {

        // 1. 내 취향 유사도 순 O
        if (sortType == SortType.SORT_SIMILARITY) {

            // 1. 유저 독서 취향 반환
            ReadingPreference readingPreference = readingPreferenceRepository.findByUserId(userId).orElse(null);
            if(!readingPreference.isCompleted()){return CurationListGetRes.ofEmpty(sortType);}
            ReadingPreferenceInfo preferenceInfo = ReadingPreferenceInfo.from(readingPreference);

            // 2. 매칭된 큐레이션 리스트트 조회
            List<CurationMatchResult> recommended = curationRecommendationService.recommend(preferenceInfo);

            //3. 매칭된 큐레이션 페이지네이션
            List<CurationMatchResult> paginated = CurationMatchResultPagination.paginate(recommended, cursor, PageRequest.of(0, size + 1));

            //4. 유저가 스크롤시, 다음 조회할 값있는지
            boolean hasNext = paginated.size() > size;

            // 5. 콘텐츠가 더있으면 자르고 다음페이지에서 보여줌
            //    더 없으면 그냥 보여줌
            List<CurationMatchResult> contentResults = hasNext ? paginated.subList(0, size) : paginated;

            // 6. 다음 커서 반환
            Long nextCursor = hasNext ? paginated.get(size).getCuration().getId() : null;

            // 7-1. 좋아요 여부 계산을 위한 큐레이션 ID 목록 추출
            List<Long> curationIds = contentResults.stream()
                    .map(r -> r.getCuration().getId())
                    .toList();

            // 7-2. 사용자가 좋아요 누른 큐레이션 ID 조회
            Set<Long> likedIds = curationLikeRepository
                    .findLikedCurationIds(userId, curationIds)
                    .stream()
                    .collect(Collectors.toSet());


            // 8. 큐레이션 리스트 Dto에 들어갈 단건 dto 생성
            List<CurationContentRes> content = contentResults.stream()
                    .map(result -> CurationContentRes.from(
                            result,
                            preferenceInfo,
                            likedIds.contains(result.getCuration().getId())
                    ))
                    .collect(Collectors.toList());

            //9. 큐레이션 리스트로 감싸기
            return CurationListGetRes.from(sortType, content, hasNext, nextCursor);
        }

        // 2) 내 취향 유사도순 X
        List<Curation> curations = pageHandler.getCurationsPage(userId, sortType, cursor, size, null);
        CursorPage<Curation> page = pageHandler.createCursorPage(curations, size);


        // 2-1. 좋아요 여부 계산을 위한 큐레이션 ID 목록 추출
        List<Long> curationIds = page.getContent().stream()
                .map(Curation::getId)
                .toList();

        // 2-2. 사용자가 좋아요 누른 큐레이션 ID 조회
        Set<Long> likedIds = curationLikeRepository
                .findLikedCurationIds(userId, curationIds)
                .stream()
                .collect(Collectors.toSet());


        List<CurationContentRes> content = page.getContent().stream()
                .map(c -> CurationContentRes.from(
                        c,
                        likedIds.contains(c.getId())
                ))
                .collect(Collectors.toList());
        CurationListGetRes.from(sortType, content, page.isHasNext(), page.getNextCursor());

        return CurationListGetRes.from(
                sortType,
                content,
                page.isHasNext(),
                page.getNextCursor()
        );
    }




    // Issue 1) DTO 만들어서 독서취향 정보 레이어간 소통 vs 사용자 독서취향 실시간 수정 반영 고려
    // 1. 사용자는 독서취향을 한번 설정하면 자주 바꾸지 않는다.
    // 2. 따라서 DTO 생성 후 넣기로 결정
}