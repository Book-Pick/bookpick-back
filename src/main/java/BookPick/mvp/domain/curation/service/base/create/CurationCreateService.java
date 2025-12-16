// CurationListService.java
package BookPick.mvp.domain.curation.service.base.create;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.curation.dto.base.CurationReq;
import BookPick.mvp.domain.curation.dto.base.create.CurationCreateRes;
import BookPick.mvp.domain.curation.dto.base.get.one.CurationGetRes;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateReq;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateRes;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.entity.CurationLike;
import BookPick.mvp.domain.curation.exception.common.CurationAccessDeniedException;
import BookPick.mvp.domain.curation.exception.common.CurationNotFoundException;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.repository.like.CurationLikeRepository;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.domain.user.service.subscribe.CurationSubscribeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurationCreateService {

    private final CurationRepository curationRepository;
    private final CurationLikeRepository curationLikeRepository;
    private final UserRepository userRepository;
    private final CurationSubscribeService curationSubscribeService;


    // -- 큐레이션 등록 --
    @Transactional
    public CurationCreateRes createCuration(Long userId, CurationReq req) {


        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Curation curation = Curation.from(req, user);
        if (req.isDrafted()) {
            curation.draft();
        }else{
            curation.publish();
        }
        Curation saved = curationRepository.save(curation);

        return CurationCreateRes.from(saved);

    }



}
