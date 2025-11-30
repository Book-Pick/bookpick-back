package BookPick.mvp.domain.user.service.subscribe;

import BookPick.mvp.domain.user.dto.subscribe.CuratorSubscribeReq;
import BookPick.mvp.domain.user.dto.subscribe.CuratorSubscribeRes;
import BookPick.mvp.domain.user.entity.CuratorSubscribe;
import BookPick.mvp.domain.user.exception.curator.CuratorNotFoundException;
import BookPick.mvp.domain.user.repository.subscribe.CurationSubscribeRepository;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

//    @Transactional(readOnly = true)
//    public CurationListGetRes getSubscribedCurations(Long userId, Long cursor, int size) {
//        Pageable pageable = PageRequest.of(0, size + 1);
//        List<Curation> curations = curationSubscribeRepository.findSubscribedCurationsByUserId(userId, cursor, pageable);
//
//        boolean hasNext = curations.size() > size;
//        Long nextCursor = null;
//        if (hasNext) {
//            nextCursor = curations.get(size).getId();
//            curations = curations.subList(0, size);
//        }
//
//        List<CurationContentRes> content = curations.stream()
//                .map(CurationContentRes::from)
//                .collect(Collectors.toList());
//
//        return CurationListGetRes.from(SortType.SORT_LATEST, content, hasNext, nextCursor);
//    }
}
