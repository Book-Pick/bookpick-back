// CurationService.java
package BookPick.mvp.domain.curation.service;

import BookPick.mvp.domain.curation.dto.create.CurationCreateReq;
import BookPick.mvp.domain.curation.dto.create.CurationCreateRes;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurationService {

    private final CurationRepository curationRepository;

    @Transactional
    public CurationCreateRes create(Long userId, CurationCreateReq req) {
        Curation curation = Curation.builder()
            .userId(userId)
            .thumbnailUrl(req.thumbnail().imageUrl())
            .thumbnailColor(req.thumbnail().imageColor())
            .bookTitle(req.book().title())
            .bookAuthor(req.book().author())
            .bookIsbn(req.book().isbn())
            .review(req.review())
            .moods(req.recommend().moods())
            .genres(req.recommend().genres())
            .keywords(req.recommend().keywords())
            .styles(req.recommend().styles())
            .build();

        Curation saved = curationRepository.save(curation);

        return CurationCreateRes.from(saved);
    }
}