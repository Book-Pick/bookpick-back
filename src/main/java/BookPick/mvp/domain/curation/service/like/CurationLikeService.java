package BookPick.mvp.domain.curation.service.like;

import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.entity.CurationLike;
import BookPick.mvp.domain.curation.exception.common.CurationNotFoundException;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.repository.like.CurationLikeRepository;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurationLikeService {
    private final UserRepository userRepository;
    private final CurationRepository curationRepository;
    private final CurationLikeRepository curationLikeRepository;

    // 1. 포스트 좋아요
    @Transactional
    public boolean CurationLikeOrUnlike(Long userId, Long curationId) {

        // 1. 포스트 아이디 얻기
        Curation curation =
                curationRepository
                        .findByIdWithLock(curationId)
                        .orElseThrow(CurationNotFoundException::new);
        // 2. 유저 아이디 얻기
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 3. 해당 유저가 이미 좋아요를 눌렀는지 체크
        // 3.1 유저 좋아요 정보 가져오기
        Optional<CurationLike> opt =
                curationLikeRepository.findByUserIdAndCurationId(userId, curationId);

        // 3.2 좋아요 정보가 없으면 생성 후 해당 게시글 좋아요 카운트 +1
        if (opt.isEmpty()) {
            CurationLike curationLike =
                    CurationLike.builder().user(user).curation(curation).build();

            curationLikeRepository.save(curationLike);

            curation.setLikeCount(curation.getLikeCount() + 1);

            return true;
        }

        // 3.2 좋아요 정보가 있으면 삭제 후 해당 게시글 좋아요 카운트 -1
        else {
            curationLikeRepository.delete(opt.get());

            if (curation.getLikeCount() > 0) {
                curation.setLikeCount(curation.getLikeCount() - 1);
            }

            return false;
        }
    }
}
