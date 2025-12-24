// CurationListService.java
package BookPick.mvp.domain.curation.service.base.update;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.curation.dto.base.get.one.CurationGetRes;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateReq;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateRes;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.entity.CurationLike;
import BookPick.mvp.domain.curation.exception.common.CurationAccessDeniedException;
import BookPick.mvp.domain.curation.exception.common.CurationNotFoundException;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.repository.like.CurationLikeRepository;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.domain.user.service.subscribe.CurationSubscribeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurationUpdateService {

    private final CurationRepository curationRepository;
    private final CurationLikeRepository curationLikeRepository;
    private final UserRepository userRepository;
    private final CurationSubscribeService curationSubscribeService;


    public CurationGetRes updateCuration(Long curationId, CustomUserDetails user, HttpServletRequest req) {

    }



    // 임시저장 -> 발행
    @Transactional
    public CurationUpdateRes curationUpdate(Long userId, Long curationId, CurationUpdateReq req) {
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(CurationNotFoundException::new);

        if (!curation.getUser().getId().equals(userId)) {
            throw new CurationAccessDeniedException();
        }

        curation.curationUpdate(req);   // 임시저장 및 발행 처리도 가능

        return CurationUpdateRes.from(curation);
    }


    // 임시저장 -> 발행
    @Transactional
    public CurationUpdateRes curationUpdate(Long userId, Long curationId, CurationUpdateReq req) {
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(CurationNotFoundException::new);

        if (!curation.getUser().getId().equals(userId)) {
            throw new CurationAccessDeniedException();
        }

        curation.curationUpdate(req);   // 임시저장 및 발행 처리도 가능

        return CurationUpdateRes.from(curation);
    }

}
