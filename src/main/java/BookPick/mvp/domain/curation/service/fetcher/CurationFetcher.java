package BookPick.mvp.domain.curation.service.fetcher;

import BookPick.mvp.domain.curation.enums.SortType;
import BookPick.mvp.domain.curation.model.Curation;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Component
@RequiredArgsConstructor
public class CurationFetcher {

    private final CurationRepository curationRepository;

    public List<Curation> fetchCurations(SortType sortType, Long cursor, Pageable pageable) {

        if (cursor == null) {
            if(sortType.equals(SortType.SORT_LATEST))
            return curationRepository.findAllByOrderByCreatedAtDesc(pageable);  // 취향 유사도 만들기 전까진 최신순
        }
        return switch (sortType) {
            case SORT_POPULAR -> curationRepository.findCurationsByPopularity(cursor, pageable);
            case SORT_LATEST -> curationRepository.findCurations(cursor, pageable);
            case SORT_SIMILARITY ->  curationRepository.findCurations(cursor, pageable);
        };
    }

    public Long calculateNextCursor(List<Curation> curations, int size, boolean hasNext) {
        return hasNext ? curations.get(size).getId() : null;
    }
}