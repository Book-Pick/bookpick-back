// CurationListService.java
package BookPick.mvp.domain.curation.service.base.create;

import BookPick.mvp.domain.curation.dto.base.CurationReq;
import BookPick.mvp.domain.curation.dto.base.create.CurationCreateRes;
import BookPick.mvp.domain.curation.dto.base.create.CurationCreateResult;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.repository.like.CurationLikeRepository;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.domain.user.service.subscribe.CurationSubscribeService;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurationCreateService {

    private final CurationRepository curationRepository;
    private final UserRepository userRepository;


    // 분기
    @Transactional
    public CurationCreateResult saveCuration(Long userId, CurationReq req) {


        // 발행
         if(!req.isDrafted()){
             return CurationCreateResult.from(publishNewCuration(userId, req),  SuccessCode.CURATION_PUBLISH_SUCCESS);
        }
         else{
             return CurationCreateResult.from(draftNewCuration(userId, req),  SuccessCode.CURATION_DRAFT_SUCCESS);
         }

    }
    // 큐레이션 발행
    public CurationCreateRes publishNewCuration(Long userId, CurationReq req) {


        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Curation curation = Curation.from(req, user );
        curation.publish();
        Curation saved = curationRepository.save(curation);

        return CurationCreateRes.from(saved);

    }


     // 큐레이션 임시저장
    public CurationCreateRes draftNewCuration(Long userId, CurationReq req) {


        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Curation curation = Curation.from(req, user );
        curation.draft();
        Curation saved = curationRepository.save(curation);

        return CurationCreateRes.from(saved);

    }



}
