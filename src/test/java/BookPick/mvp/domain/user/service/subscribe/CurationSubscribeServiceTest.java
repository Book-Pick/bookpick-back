package BookPick.mvp.domain.user.service.subscribe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import BookPick.mvp.domain.user.dto.subscribe.CuratorSubscribeReq;
import BookPick.mvp.domain.user.dto.subscribe.CuratorSubscribeRes;
import BookPick.mvp.domain.user.dto.subscribe.SubscribedCuratorPageRes;
import BookPick.mvp.domain.user.entity.CuratorSubscribe;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.domain.user.repository.subscribe.CurationSubscribeRepository;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("큐레이터 구독 서비스 테스트")
class CurationSubscribeServiceTest {

    @InjectMocks private CurationSubscribeService curationSubscribeService;

    @Mock private UserRepository userRepository;

    @Mock private CurationSubscribeRepository curationSubscribeRepository;

    @Test
    @DisplayName("큐레이터 구독 성공")
    void subscribe_success_newSubscription() {
        // given
        Long userId = 1L;
        Long curatorId = 2L;
        CuratorSubscribeReq req = new CuratorSubscribeReq(curatorId);
        User user = User.builder().id(userId).build();
        User curator = User.builder().id(curatorId).nickname("curator").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(curatorId)).thenReturn(Optional.of(curator));
        when(curationSubscribeRepository.findByUserIdAndCuratorId(userId, curatorId))
                .thenReturn(Optional.empty());

        // when
        CuratorSubscribeRes res = curationSubscribeService.subscribe(userId, req);

        // then
        assertThat(res.subscribed()).isTrue();
        assertThat(res.curatorId()).isEqualTo(curatorId);
        verify(curationSubscribeRepository).save(any(CuratorSubscribe.class));
    }

    @Test
    @DisplayName("큐레이터 구독 취소 성공")
    void subscribe_success_cancelSubscription() {
        // given
        Long userId = 1L;
        Long curatorId = 2L;
        CuratorSubscribeReq req = new CuratorSubscribeReq(curatorId);
        User user = User.builder().id(userId).build();
        User curator = User.builder().id(curatorId).nickname("curator").build();
        CuratorSubscribe existingSubscription =
                CuratorSubscribe.builder().user(user).curator(curator).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(curatorId)).thenReturn(Optional.of(curator));
        when(curationSubscribeRepository.findByUserIdAndCuratorId(userId, curatorId))
                .thenReturn(Optional.of(existingSubscription));

        // when
        CuratorSubscribeRes res = curationSubscribeService.subscribe(userId, req);

        // then
        assertThat(res.subscribed()).isFalse();
        verify(curationSubscribeRepository).delete(existingSubscription);
    }

    @Test
    @DisplayName("구독 여부 확인 - 구독 중")
    void isSubscribeCurator_true() {
        // given
        Long userId = 1L;
        Long curatorId = 2L;
        when(curationSubscribeRepository.findByUserIdAndCuratorId(userId, curatorId))
                .thenReturn(Optional.of(new CuratorSubscribe()));

        // when
        boolean isSubscribed = curationSubscribeService.isSubscribeCurator(userId, curatorId);

        // then
        assertThat(isSubscribed).isTrue();
    }

    @Test
    @DisplayName("구독 여부 확인 - 구독 안함")
    void isSubscribeCurator_false() {
        // given
        Long userId = 1L;
        Long curatorId = 2L;
        when(curationSubscribeRepository.findByUserIdAndCuratorId(userId, curatorId))
                .thenReturn(Optional.empty());

        // when
        boolean isSubscribed = curationSubscribeService.isSubscribeCurator(userId, curatorId);

        // then
        assertThat(isSubscribed).isFalse();
    }

    @Test
    @DisplayName("구독한 큐레이터 목록 조회")
    void getSubscribedCurators_success() {
        // given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        User curatorUser = User.builder().id(2L).nickname("curator1").build();
        CuratorSubscribe subscription = CuratorSubscribe.builder().curator(curatorUser).build();
        Page<CuratorSubscribe> page =
                new PageImpl<>(Collections.singletonList(subscription), pageable, 1);

        when(curationSubscribeRepository.findByUserIdOrderByIdDesc(userId, pageable))
                .thenReturn(page);

        // when
        SubscribedCuratorPageRes res =
                curationSubscribeService.getSubscribedCurators(userId, 0, 10);

        // then
        assertThat(res.curators()).hasSize(1);
        assertThat(res.curators().get(0).nickname()).isEqualTo("curator1");
        assertThat(res.pageInfo().hasNext()).isFalse();
    }
}
