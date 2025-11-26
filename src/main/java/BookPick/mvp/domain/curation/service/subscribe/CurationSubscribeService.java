package BookPick.mvp.domain.curation.service.subscribe;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.curation.dto.subscribe.CurationSubscribeDto;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.entity.CurationLike;
import BookPick.mvp.domain.curation.entity.CurationSubscribe;
import BookPick.mvp.domain.curation.exception.CurationNotFoundException;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.repository.subscribe.CurationSubscribeRepository;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurationSubscribeService {
    private final CurationSubscribeRepository curationSubscribeRepository;
    private final CurationRepository curationRepository;
    private final UserRepository userRepository;


    // 1. 큐레이션 구독
    @Transactional
    public CurationSubscribeDto subscribe(Long userId, Long curationId) {

        // 1. 포스트 아이디 얻기
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(CurationNotFoundException::new);
        // 2. 유저 아이디 얻기
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 3. 해당 유저가 구독했는지 체크
        // 3.1 유저의 큐레이션 구독 정보 가져오기
        Optional<CurationSubscribe> opt = curationSubscribeRepository.findByUserIdAndCurationId(userId, curationId);

        // 3.2 구독 정보가 없으면 생성
        if (opt.isEmpty()) {
            CurationSubscribe curationSubscribe = CurationSubscribe.builder()
                    .user(user)
                    .curation(curation)
                    .build();

            curationSubscribeRepository.save(curationSubscribe);

            return CurationSubscribeDto.from(curationSubscribe , true);
        }

        // 3.2 구독 정보가 있으면 삭제
        else {
            curationSubscribeRepository.delete(opt.get());

            return CurationSubscribeDto.from(curation , false);
        }
    }

}
