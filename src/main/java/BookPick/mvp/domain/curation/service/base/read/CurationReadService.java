// CurationListService.java
package BookPick.mvp.domain.curation.service.base.read;

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
public class CurationReadService {

    private final CurationRepository curationRepository;
    private final CurationLikeRepository curationLikeRepository;
    private final UserRepository userRepository;
    private final CurationSubscribeService curationSubscribeService;





    // -- 큐레이션 단건 조회 --
    @Transactional
    public CurationGetRes findCuration(Long curationId, CustomUserDetails user, HttpServletRequest req) {
        boolean isLikedCuration = false;
        boolean isSubscribedCurator = false;
        CurationGetRes res;

        Curation curation = curationRepository.findByIdWithUser(curationId)
                .orElseThrow(CurationNotFoundException::new);

        curation.increaseViewCount();   // 큐레이션 조회수 +1


        if (user != null) {
            // 1. 좋아요 정보 찾기
            Optional<CurationLike> curationLike = curationLikeRepository.findByUserIdAndCurationId(user.getId(), curationId);
            if (curationLike.isPresent()) {
                isLikedCuration = true;
            }

            // 2. 큐레이터 구독 여부 조회
            isSubscribedCurator = curationSubscribeService.isSubscribeCurator(user.getId(), curation.getUser().getId());

//            // 3. 큐레이션 작성자면 책 정보 넣어서 큐레이션 반환
//            if (curation.getUser().getId().equals(user.getId())) {
//                return CurationGetRes.fromOwnerView(curation, isSubscribedCurator, isLikedCuration);
//            }
        }

        return CurationGetRes.from(curation, isSubscribedCurator, isLikedCuration);
    }






}
