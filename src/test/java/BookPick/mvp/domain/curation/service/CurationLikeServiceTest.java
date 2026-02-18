package BookPick.mvp.domain.curation.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.entity.CurationLike;
import BookPick.mvp.domain.curation.exception.common.CurationNotFoundException;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.repository.like.CurationLikeRepository;
import BookPick.mvp.domain.curation.service.like.CurationLikeService;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("큐레이션 좋아요 서비스 테스트")
class CurationLikeServiceTest {

    @InjectMocks private CurationLikeService curationLikeService;

    @Mock private CurationRepository curationRepository;

    @Mock private UserRepository userRepository;

    @Mock private CurationLikeRepository curationLikeRepository;

    @Test
    @DisplayName("좋아요 추가 성공")
    void likeCuration_success() {
        // given
        Long userId = 1L;
        Long curationId = 1L;

        User mockUser = User.builder().id(userId).email("test@test.com").build();

        Curation mockCuration = Curation.builder().id(curationId).likeCount(0).build();

        when(curationRepository.findByIdWithLock(curationId)).thenReturn(Optional.of(mockCuration));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(curationLikeRepository.findByUserIdAndCurationId(userId, curationId))
                .thenReturn(Optional.empty());
        when(curationLikeRepository.save(any(CurationLike.class)))
                .thenReturn(CurationLike.builder().build());

        // when
        boolean result = curationLikeService.CurationLikeOrUnlike(userId, curationId);

        // then
        assertThat(result).isTrue();
        assertThat(mockCuration.getLikeCount()).isEqualTo(1);

        verify(curationRepository).findByIdWithLock(curationId);
        verify(curationLikeRepository).save(any(CurationLike.class));
        verify(curationLikeRepository, never()).delete(any());
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void unlikeCuration_success() {
        // given
        Long userId = 1L;
        Long curationId = 1L;

        User mockUser = User.builder().id(userId).email("test@test.com").build();

        Curation mockCuration = Curation.builder().id(curationId).likeCount(5).build();

        CurationLike existingLike =
                CurationLike.builder().user(mockUser).curation(mockCuration).build();

        when(curationRepository.findByIdWithLock(curationId)).thenReturn(Optional.of(mockCuration));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(curationLikeRepository.findByUserIdAndCurationId(userId, curationId))
                .thenReturn(Optional.of(existingLike));

        // when
        boolean result = curationLikeService.CurationLikeOrUnlike(userId, curationId);

        // then
        assertThat(result).isFalse();
        assertThat(mockCuration.getLikeCount()).isEqualTo(4);

        verify(curationRepository).findByIdWithLock(curationId);
        verify(curationLikeRepository).delete(existingLike);
        verify(curationLikeRepository, never()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 큐레이션 - 예외 발생")
    void likeCuration_curationNotFound() {
        // given
        Long userId = 1L;
        Long curationId = 999L;

        when(curationRepository.findByIdWithLock(curationId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> curationLikeService.CurationLikeOrUnlike(userId, curationId))
                .isInstanceOf(CurationNotFoundException.class);

        verify(curationRepository).findByIdWithLock(curationId);
        verify(curationLikeRepository, never()).save(any());
        verify(curationLikeRepository, never()).delete(any());
    }

    @Test
    @DisplayName("존재하지 않는 유저 - 예외 발생")
    void likeCuration_userNotFound() {
        // given
        Long userId = 999L;
        Long curationId = 1L;

        Curation mockCuration = Curation.builder().id(curationId).build();

        when(curationRepository.findByIdWithLock(curationId)).thenReturn(Optional.of(mockCuration));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> curationLikeService.CurationLikeOrUnlike(userId, curationId))
                .isInstanceOf(UserNotFoundException.class);

        verify(curationLikeRepository, never()).save(any());
        verify(curationLikeRepository, never()).delete(any());
    }

    @Test
    @DisplayName("좋아요 카운트가 0일 때 취소해도 음수 안됨")
    void unlikeCuration_countNotNegative() {
        // given
        Long userId = 1L;
        Long curationId = 1L;

        User mockUser = User.builder().id(userId).build();
        Curation mockCuration =
                Curation.builder()
                        .id(curationId)
                        .likeCount(0) // 이미 0
                        .build();

        CurationLike existingLike =
                CurationLike.builder().user(mockUser).curation(mockCuration).build();

        when(curationRepository.findByIdWithLock(curationId)).thenReturn(Optional.of(mockCuration));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(curationLikeRepository.findByUserIdAndCurationId(userId, curationId))
                .thenReturn(Optional.of(existingLike));

        // when
        curationLikeService.CurationLikeOrUnlike(userId, curationId);

        // then
        assertThat(mockCuration.getLikeCount()).isEqualTo(0); // 음수 아님
    }

    @Test
    @DisplayName("비관적 락 사용 확인")
    void likeCuration_usesPessimisticLock() {
        // given
        Long userId = 1L;
        Long curationId = 1L;

        User mockUser = User.builder().id(userId).build();
        Curation mockCuration = Curation.builder().id(curationId).likeCount(0).build();

        when(curationRepository.findByIdWithLock(curationId)).thenReturn(Optional.of(mockCuration));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(curationLikeRepository.findByUserIdAndCurationId(userId, curationId))
                .thenReturn(Optional.empty());
        when(curationLikeRepository.save(any())).thenReturn(CurationLike.builder().build());

        // when
        curationLikeService.CurationLikeOrUnlike(userId, curationId);

        // then
        verify(curationRepository).findByIdWithLock(curationId); // 비관적 락 메서드 사용
        verify(curationRepository, never()).findById(curationId); // 일반 findById 사용 안함
    }
}
