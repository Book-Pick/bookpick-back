package BookPick.mvp.domain.curation.service.list;

import BookPick.mvp.domain.curation.dto.base.get.list.CurationContentRes;
import BookPick.mvp.domain.curation.dto.base.get.list.CurationListGetRes;
import BookPick.mvp.domain.curation.dto.base.get.list.CursorPage;
import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.enums.SortType;
import BookPick.mvp.domain.curation.model.Curation;
import BookPick.mvp.domain.curation.util.gemini.dto.CurationMatchResult;
import BookPick.mvp.domain.curation.util.list.Handler.CurationPageHandler;
import BookPick.mvp.domain.curation.util.list.similarity.CurationMatchResultPagination;
import BookPick.mvp.domain.preference.Exception.UserReadingPreferenceNotExisted;
import BookPick.mvp.domain.preference.repository.ReadingPreferenceRepository;
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

        if (sortType == SortType.SORT_SIMILARITY) {
            ReadingPreferenceInfo preferenceInfo = readingPreferenceRepository.findByUserId(userId)
                    .map(ReadingPreferenceInfo::from)
                    .orElseThrow(UserReadingPreferenceNotExisted::new);

            List<CurationMatchResult> recommended = curationRecommendationService.recommend(preferenceInfo);
            List<CurationMatchResult> paginated = CurationMatchResultPagination.paginate(recommended, cursor, PageRequest.of(0, size + 1));

            boolean hasNext = paginated.size() > size;
            List<CurationMatchResult> contentResults = hasNext ? paginated.subList(0, size) : paginated;
            Long nextCursor = hasNext ? paginated.get(size).getCuration().getId() : null;

            List<CurationContentRes> content = contentResults.stream()
                    .map(CurationContentRes::from)
                    .collect(Collectors.toList());

            return CurationListGetRes.from(sortType, content, hasNext, nextCursor);
        }

        // 1) 큐레이션 페이징해서 구분
        List<Curation> curations = pageHandler.getCurationsPage(sortType, cursor, size, null);
        CursorPage<Curation> page = pageHandler.createCursorPage(curations, size);
        List<CurationContentRes> content = pageHandler.convertToContentRes(page.getContent());

        return CurationListGetRes.from(sortType, content, page.isHasNext(), page.getNextCursor());
    }

    // Issue 1) DTO 만들어서 독서취향 정보 레이어간 소통 vs 사용자 독서취향 실시간 수정 반영 고려
    // 1. 사용자는 독서취향을 한번 설정하면 자주 바꾸지 않는다.
    // 2. 따라서 DTO 생성 후 넣기로 결정
}