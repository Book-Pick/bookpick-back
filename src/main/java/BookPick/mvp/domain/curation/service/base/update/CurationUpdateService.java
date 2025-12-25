// CurationListService.java
package BookPick.mvp.domain.curation.service.base.update;

import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateReq;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateRes;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateResult;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.exception.common.CurationAccessDeniedException;
import BookPick.mvp.domain.curation.exception.common.CurationAlreadyPublishedException;
import BookPick.mvp.domain.curation.exception.common.CurationNotFoundException;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.repository.like.CurationLikeRepository;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.domain.user.service.subscribe.CurationSubscribeService;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurationUpdateService {

    private final CurationRepository curationRepository;
    private final CurationLikeRepository curationLikeRepository;
    private final UserRepository userRepository;
    private final CurationSubscribeService curationSubscribeService;

    @Transactional
    public CurationUpdateResult updateCuration(Long userId, Long curationId, CurationUpdateReq req) {
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(CurationNotFoundException::new);

        if (curation.getIsDrafted()) {
            if (req.isDrafted()) {
                // 임시저장 -> 임시저장
                return CurationUpdateResult.from(reDraftCuration(userId, curation, req), SuccessCode.CURATION_DRAFT_UPDATE_SUCCESS);
            } else {
                // 임시저장 -> 발행본
                return CurationUpdateResult.from(publishDraftedCuration(userId, curation, req), SuccessCode.DRAFTED_CURATION_PUBLISH_SUCCESS);
            }
        }


        // 발행본 -> 발행본
        else {
            if (!req.isDrafted()) {
                return CurationUpdateResult.from(modifyPublishedCuration(userId, curation, req), SuccessCode.CURATION_UPDATE_SUCCESS);
            } else {
                throw new CurationAlreadyPublishedException();
            }
        }

    }



    /*---------------------------------------------------------------------------------------------------*/

    // 임시저장 -> 임시저장
    public CurationUpdateRes reDraftCuration(Long userId, Curation curation, CurationUpdateReq req) {

        if (!curation.getUser().getId().equals(userId)) {
            throw new CurationAccessDeniedException();
        }


        curation.curationUpdate(req);   // 임시저장 및 발행 처리도 가능
        curation.draft();

        return CurationUpdateRes.from(curation);
    }

    // 임시저장 -> 발행본
    public CurationUpdateRes publishDraftedCuration(Long userId, Curation curation, CurationUpdateReq req) {


        if (!curation.getUser().getId().equals(userId)) {
            throw new CurationAccessDeniedException();
        }

        curation.curationUpdate(req);   // 임시저장 및 발행 처리도 가능
        curation.publish();

        return CurationUpdateRes.from(curation);
    }

    // 발행 -> 발행
    public CurationUpdateRes modifyPublishedCuration(Long userId, Curation curation, CurationUpdateReq req) {


        if (!curation.getUser().getId().equals(userId)) {
            throw new CurationAccessDeniedException();
        }

        curation.curationUpdate(req);   // 임시저장 및 발행 처리도 가능

        return CurationUpdateRes.from(curation);
    }

}
