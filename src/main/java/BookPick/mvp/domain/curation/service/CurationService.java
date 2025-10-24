// CurationService.java
package BookPick.mvp.domain.curation.service;

import BookPick.mvp.domain.curation.dto.create.CurationCreateReq;
import BookPick.mvp.domain.curation.dto.create.CurationCreateRes;
import BookPick.mvp.domain.curation.dto.get.CurationGetRes;
import BookPick.mvp.domain.curation.dto.update.CurationUpdateReq;
import BookPick.mvp.domain.curation.dto.update.CurationUpdateRes;
import BookPick.mvp.domain.curation.dto.delete.CurationDeleteRes;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.exception.CurationAccessDeniedException;
import BookPick.mvp.domain.curation.exception.CurationNotFoundException;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CurationService {

    private final CurationRepository curationRepository;

    // -- 큐레이션 등록 --
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

    // -- 큐레이션 단건 조회 --
    @Transactional(readOnly = true)
    public CurationGetRes findCuration(Long curationId) {
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(CurationNotFoundException::new);

        return CurationGetRes.from(curation);
    }

    // -- 큐레이션 수정 --
    @Transactional
    public CurationUpdateRes modifyCuration(Long userId, Long curationId, CurationUpdateReq req) {
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(CurationNotFoundException::new);

        if (!curation.getUserId().equals(userId)) {
            throw new CurationAccessDeniedException();
        }

        curation.update(req);

        return CurationUpdateRes.from(curation);
    }

    // -- 큐레이션 삭제 --
    @Transactional
    public CurationDeleteRes removeCuration(Long userId, Long curationId) {
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(CurationNotFoundException::new);

        if (!curation.getUserId().equals(userId)) {
            throw new CurationAccessDeniedException();
        }

        curationRepository.delete(curation);

        return CurationDeleteRes.from(curation.getId(), LocalDateTime.now());
    }
}