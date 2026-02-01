package BookPick.mvp.domain.curation.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import BookPick.mvp.domain.curation.dto.base.delete.CurationDeleteRes;
import BookPick.mvp.domain.curation.dto.base.delete.CurationListDeleteReq;
import BookPick.mvp.domain.curation.dto.base.delete.CurationListDeleteRes;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.exception.common.CurationAccessDeniedException;
import BookPick.mvp.domain.curation.exception.common.CurationNotFoundException;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.repository.like.CurationLikeRepository;
import BookPick.mvp.domain.curation.service.base.delete.CurationDeleteService;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.domain.user.service.subscribe.CurationSubscribeService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("큐레이션 삭제 서비스 테스트")
class CurationDeleteServiceTest {

    @InjectMocks private CurationDeleteService curationDeleteService;

    @Mock private CurationRepository curationRepository;

    @Mock private CurationLikeRepository curationLikeRepository;

    @Mock private UserRepository userRepository;

    @Mock private CurationSubscribeService curationSubscribeService;

    @Test
    @DisplayName("큐레이션 단건 삭제 성공")
    void removeCuration_success() {
        // given
        Long userId = 1L;
        Long curationId = 1L;

        User mockUser = User.builder().id(userId).email("test@test.com").build();

        Curation mockCuration =
                Curation.builder()
                        .id(curationId)
                        .user(mockUser)
                        .title("삭제할 큐레이션")
                        .isDrafted(false)
                        .likeCount(0)
                        .viewCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        when(curationRepository.findById(curationId)).thenReturn(Optional.of(mockCuration));
        doNothing().when(curationRepository).delete(mockCuration);

        // when
        CurationDeleteRes result = curationDeleteService.removeCuration(userId, curationId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.curationIds()).isEqualTo(curationId);
        assertThat(result.deletedAt()).isNotNull();

        verify(curationRepository).findById(curationId);
        verify(curationRepository).delete(mockCuration);
    }

    @Test
    @DisplayName("큐레이션 단건 삭제 - 다른 사용자가 삭제 시도 시 예외 발생")
    void removeCuration_notOwner_fail() {
        // given
        Long ownerId = 1L;
        Long attackerId = 2L;
        Long curationId = 1L;

        User owner = User.builder().id(ownerId).email("owner@test.com").build();

        Curation mockCuration =
                Curation.builder()
                        .id(curationId)
                        .user(owner)
                        .title("큐레이션")
                        .isDrafted(false)
                        .likeCount(0)
                        .viewCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        when(curationRepository.findById(curationId)).thenReturn(Optional.of(mockCuration));

        // when & then
        assertThatThrownBy(() -> curationDeleteService.removeCuration(attackerId, curationId))
                .isInstanceOf(CurationAccessDeniedException.class);

        verify(curationRepository).findById(curationId);
        verify(curationRepository, never()).delete(any(Curation.class));
    }

    @Test
    @DisplayName("큐레이션 단건 삭제 - 존재하지 않는 큐레이션")
    void removeCuration_notFound_fail() {
        // given
        Long userId = 1L;
        Long curationId = 999L;

        when(curationRepository.findById(curationId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> curationDeleteService.removeCuration(userId, curationId))
                .isInstanceOf(CurationNotFoundException.class);

        verify(curationRepository).findById(curationId);
        verify(curationRepository, never()).delete(any(Curation.class));
    }

    @Test
    @DisplayName("큐레이션 복수 삭제 성공")
    void removeCurations_success() {
        // given
        Long userId = 1L;
        List<Long> curationIds = List.of(1L, 2L, 3L);

        User mockUser = User.builder().id(userId).email("test@test.com").build();

        Curation curation1 =
                Curation.builder()
                        .id(1L)
                        .user(mockUser)
                        .title("큐레이션 1")
                        .isDrafted(false)
                        .likeCount(0)
                        .viewCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        Curation curation2 =
                Curation.builder()
                        .id(2L)
                        .user(mockUser)
                        .title("큐레이션 2")
                        .isDrafted(false)
                        .likeCount(0)
                        .viewCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        Curation curation3 =
                Curation.builder()
                        .id(3L)
                        .user(mockUser)
                        .title("큐레이션 3")
                        .isDrafted(true)
                        .likeCount(0)
                        .viewCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        List<Curation> curations = List.of(curation1, curation2, curation3);

        CurationListDeleteReq req = new CurationListDeleteReq(curationIds);

        when(curationRepository.findByIdIn(curationIds)).thenReturn(curations);
        doNothing().when(curationRepository).delete(any(Curation.class));

        // when
        CurationListDeleteRes result = curationDeleteService.removeCurations(userId, req);

        // then
        assertThat(result).isNotNull();
        assertThat(result.ids()).hasSize(3);
        assertThat(result.ids()).containsExactlyInAnyOrder(1L, 2L, 3L);
        assertThat(result.deletedAt()).isNotNull();

        verify(curationRepository).findByIdIn(curationIds);
        verify(curationRepository, times(3)).delete(any(Curation.class));
    }

    @Test
    @DisplayName("큐레이션 복수 삭제 - 일부 큐레이션이 존재하지 않음")
    void removeCurations_partialNotFound_fail() {
        // given
        Long userId = 1L;
        List<Long> requestedIds = List.of(1L, 2L, 3L);

        User mockUser = User.builder().id(userId).email("test@test.com").build();

        // 2개만 존재 (3번은 없음)
        Curation curation1 =
                Curation.builder()
                        .id(1L)
                        .user(mockUser)
                        .title("큐레이션 1")
                        .isDrafted(false)
                        .likeCount(0)
                        .viewCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        Curation curation2 =
                Curation.builder()
                        .id(2L)
                        .user(mockUser)
                        .title("큐레이션 2")
                        .isDrafted(false)
                        .likeCount(0)
                        .viewCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        List<Curation> foundCurations = List.of(curation1, curation2);

        CurationListDeleteReq req = new CurationListDeleteReq(requestedIds);

        when(curationRepository.findByIdIn(requestedIds)).thenReturn(foundCurations);

        // when & then
        assertThatThrownBy(() -> curationDeleteService.removeCurations(userId, req))
                .isInstanceOf(CurationNotFoundException.class);

        verify(curationRepository).findByIdIn(requestedIds);
        verify(curationRepository, never()).delete(any(Curation.class));
    }

    @Test
    @DisplayName("큐레이션 복수 삭제 - 다른 사용자의 큐레이션 포함")
    void removeCurations_containsOthersPost_fail() {
        // given
        Long userId = 1L;
        Long otherUserId = 2L;
        List<Long> curationIds = List.of(1L, 2L);

        User mockUser = User.builder().id(userId).email("test@test.com").build();

        User otherUser = User.builder().id(otherUserId).email("other@test.com").build();

        Curation myCuration =
                Curation.builder()
                        .id(1L)
                        .user(mockUser)
                        .title("내 큐레이션")
                        .isDrafted(false)
                        .likeCount(0)
                        .viewCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        Curation otherCuration =
                Curation.builder()
                        .id(2L)
                        .user(otherUser)
                        .title("다른 사람 큐레이션")
                        .isDrafted(false)
                        .likeCount(0)
                        .viewCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        List<Curation> curations = List.of(myCuration, otherCuration);

        CurationListDeleteReq req = new CurationListDeleteReq(curationIds);

        when(curationRepository.findByIdIn(curationIds)).thenReturn(curations);

        // when & then
        assertThatThrownBy(() -> curationDeleteService.removeCurations(userId, req))
                .isInstanceOf(CurationAccessDeniedException.class);

        verify(curationRepository).findByIdIn(curationIds);
        // 첫 번째 큐레이션은 삭제되지만, 두 번째에서 예외 발생하므로 1번만 호출됨
        verify(curationRepository).delete(myCuration);
        verify(curationRepository, times(1)).delete(any(Curation.class));
    }

    @Test
    @DisplayName("큐레이션 복수 삭제 - 빈 리스트")
    void removeCurations_emptyList_success() {
        // given
        Long userId = 1L;
        List<Long> emptyIds = List.of();

        CurationListDeleteReq req = new CurationListDeleteReq(emptyIds);

        when(curationRepository.findByIdIn(emptyIds)).thenReturn(List.of());

        // when
        CurationListDeleteRes result = curationDeleteService.removeCurations(userId, req);

        // then
        assertThat(result).isNotNull();
        assertThat(result.ids()).isEmpty();

        verify(curationRepository).findByIdIn(emptyIds);
        verify(curationRepository, never()).delete(any(Curation.class));
    }
}
