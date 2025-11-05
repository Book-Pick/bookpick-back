package BookPick.mvp.domain.curation.service.provider;


import BookPick.mvp.domain.curation.model.Curation;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CurationPopularProvider {

    private final CurationRepository curationRepository;

    public List<Curation> fetch(Long cursor, Pageable pageable) {
        return curationRepository.findCurationsByPopularity(cursor, pageable);
    }
}

