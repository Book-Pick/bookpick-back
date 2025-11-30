package BookPick.mvp.domain.user.service.subscribe;

import BookPick.mvp.domain.user.dto.subscribe.CuratorSubscribeReq;
import BookPick.mvp.domain.user.dto.subscribe.CuratorSubscribeRes;
import BookPick.mvp.domain.user.dto.subscribe.SubscribedCuratorPageRes;
import BookPick.mvp.domain.user.dto.subscribe.SubscribedCuratorRes;
import BookPick.mvp.domain.user.entity.CuratorSubscribe;
import BookPick.mvp.domain.user.exception.curator.CuratorNotFoundException;
import BookPick.mvp.domain.user.repository.subscribe.CurationSubscribeRepository;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.global.dto.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurationSubscribeService {
    private final CurationSubscribeRepository curationSubscribeRepository;
    private final UserRepository userRepository;


    // 1. 큐레이션 구독
    @Transactional
    public CuratorSubscribeRes subscribe(Long userId, CuratorSubscribeReq req) {


        // 2. 구독 신청한 유저  얻기
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 3. 구독할 유저  얻기
        User curator = userRepository.findById(req.curatorId())
                .orElseThrow(CuratorNotFoundException::new);

        // 4. 해당 유저가 구독했는지 체크
        // 3.1 유저의 큐레이션 구독 정보 가져오기
        Optional<CuratorSubscribe> opt = curationSubscribeRepository.findByUserIdAndCuratorId(userId, req.curatorId());

        // 3.2 구독 정보가 없으면 생성
        if (opt.isEmpty()) {
            CuratorSubscribe curationSubscribe = CuratorSubscribe.builder()
                    .user(user)
                    .curator(curator)
                    .build();

            curationSubscribeRepository.save(curationSubscribe);

            return CuratorSubscribeRes.from(curationSubscribe , true);
        }

        // 3.2 구독 정보가 있으면 삭제
        else {
            curationSubscribeRepository.delete(opt.get());

            return CuratorSubscribeRes.from(curator , false);
        }
    }

    // 2. 큐레이터 구독 리스트 반환
    @Transactional(readOnly = true)
    public SubscribedCuratorPageRes getSubscribedCurators(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CuratorSubscribe> subscribesPage = curationSubscribeRepository.findByUserIdOrderByIdDesc(userId, pageable);

        List<SubscribedCuratorRes> content = subscribesPage.getContent().stream()
                .map(subscribe -> SubscribedCuratorRes.from(subscribe.getCurator()))
                .collect(Collectors.toList());

        PageInfo pageInfo = PageInfo.of(subscribesPage);

        return SubscribedCuratorPageRes.of(content, pageInfo);
    }
}