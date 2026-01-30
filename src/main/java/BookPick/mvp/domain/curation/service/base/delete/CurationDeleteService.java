// CurationListService.java
package BookPick.mvp.domain.curation.service.base.delete;

import BookPick.mvp.domain.curation.dto.base.delete.CurationDeleteRes;
import BookPick.mvp.domain.curation.dto.base.delete.CurationListDeleteReq;
import BookPick.mvp.domain.curation.dto.base.delete.CurationListDeleteRes;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.exception.common.CurationAccessDeniedException;
import BookPick.mvp.domain.curation.exception.common.CurationNotFoundException;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.repository.like.CurationLikeRepository;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.domain.user.service.subscribe.CurationSubscribeService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurationDeleteService {

    private final CurationRepository curationRepository;
    private final CurationLikeRepository curationLikeRepository;
    private final UserRepository userRepository;
    private final CurationSubscribeService curationSubscribeService;

    // -- 큐레이션 하드 삭제 --
    @Transactional
    public CurationDeleteRes removeCuration(Long userId, Long curationId) {
        Curation curation =
                curationRepository.findById(curationId).orElseThrow(CurationNotFoundException::new);

        if (!curation.getUser().getId().equals(userId)) {
            throw new CurationAccessDeniedException();
        }

        curationRepository.delete(curation);

        return CurationDeleteRes.from(curation.getId(), LocalDateTime.now());
    }

    // -- 큐레이션 복수 삭제 --
    @Transactional
    public CurationListDeleteRes removeCurations(Long userId, CurationListDeleteReq req) {

        // 1. 큐레이션 Id들 가지고 큐레이션 리스트 찾기
        List<Curation> curations = curationRepository.findByIdIn((req.curationIds()));

        // 2. 큐레이션들이 존재하지 않으면 삭제할 큐레이션을 찾을 수 없습니다.
        if (curations.size() != req.curationIds().size()) {
            throw new CurationNotFoundException();
        }

        for (Curation curation : curations) {
            if (!curation.getUser().getId().equals(userId)) {
                throw new CurationAccessDeniedException();
            }
            curationRepository.delete(curation);
        }

        List<Long> deletedIds = curations.stream().map(Curation::getId).toList();

        return CurationListDeleteRes.from(deletedIds, LocalDateTime.now());
    }
}
