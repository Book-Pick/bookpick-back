package BookPick.mvp.domain.curation.service.list;

import BookPick.mvp.domain.curation.dto.base.get.list.CurationContentRes;
import BookPick.mvp.domain.curation.dto.base.get.list.CurationListGetRes;
import BookPick.mvp.domain.curation.dto.base.get.list.CursorPage;
import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.enums.common.SortType;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.util.gemini.dto.CurationMatchResult;
import BookPick.mvp.domain.curation.util.list.Handler.CurationPageHandler;
import BookPick.mvp.domain.curation.util.list.similarity.CurationMatchResultPagination;
import BookPick.mvp.domain.ReadingPreference.Exception.UserReadingPreferenceNotExisted;
import BookPick.mvp.domain.ReadingPreference.repository.ReadingPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurationListService {

    private final CurationPageHandler pageHandler;
    private final ReadingPreferenceRepository readingPreferenceRepository;
    private final CurationRecommendationService curationRecommendationService;

    // 1. 큐레이션 리스트 조회
    public CurationListGetRes getCurations(SortType sortType, Long cursor, int size, Long userId) {


        // 1. 내 취향 유사도 순 O
        if (sortType == SortType.SORT_SIMILARITY) {

            // 1. 유저 독서 취향 반환
            ReadingPreferenceInfo preferenceInfo = readingPreferenceRepository.findByUserId(userId)
                    .map(ReadingPreferenceInfo::from)
                    .orElseThrow(UserReadingPreferenceNotExisted::new);

            // 2. 매칭된 큐레이션 리스트트 조회
            List<CurationMatchResult> recommended = curationRecommendationService.recommend(preferenceInfo);

            //3. 매칭된 큐레이션 페이지네이션
            List<CurationMatchResult> paginated = CurationMatchResultPagination.paginate(recommended, cursor, PageRequest.of(0, size + 1));

            //4. 유저가 스크롤시, 다음 조회할 값있는지
            boolean hasNext = paginated.size() > size;

            // 5. 콘텐츠가 더있으면 자르고 다음페이지에서 보여줌
            //    더 없으면 그냥 보여줌
            List<CurationMatchResult> contentResults = hasNext ? paginated.subList(0, size) : paginated;

            //6. 다음 커서 반환
            Long nextCursor = hasNext ? paginated.get(size).getCuration().getId() : null;


            //7. 큐레이션 단건 응답 포멧 반환
            List<CurationContentRes> content = contentResults.stream()
                    .map(result -> CurationContentRes.from(result, preferenceInfo))
                    .collect(Collectors.toList());

            //8. 큐레이션 리스트로 감싸기
            return CurationListGetRes.from(sortType, content, hasNext, nextCursor);
        }

        // 2) 내 취향 유사도순 X
        List<Curation> curations = pageHandler.getCurationsPage(userId, sortType, cursor, size, null);
        CursorPage<Curation> page = pageHandler.createCursorPage(curations, size);
        List<CurationContentRes> content = pageHandler.convertToContentRes(page.getContent());

        return CurationListGetRes.from(sortType, content, page.isHasNext(), page.getNextCursor());
    }

    // Issue 1) DTO 만들어서 독서취향 정보 레이어간 소통 vs 사용자 독서취향 실시간 수정 반영 고려
    // 1. 사용자는 독서취향을 한번 설정하면 자주 바꾸지 않는다.
    // 2. 따라서 DTO 생성 후 넣기로 결정
}