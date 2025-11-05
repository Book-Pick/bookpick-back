package BookPick.mvp.domain.curation.service.provider;


import BookPick.mvp.domain.curation.model.Curation;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CurationSimilarityProvider {

    private final CurationRepository curationRepository;

    public List<Curation> fetch(Long cursor, Pageable pageable) {
        // TODO: 유사도 알고리즘 구현 후 변경 예정
        return curationRepository.findCurations(cursor, pageable);
    }
}
