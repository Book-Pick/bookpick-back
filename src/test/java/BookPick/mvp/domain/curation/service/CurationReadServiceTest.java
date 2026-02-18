package BookPick.mvp.domain.curation.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.curation.dto.base.get.one.CurationGetRes;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.entity.CurationLike;
import BookPick.mvp.domain.curation.exception.common.CurationAccessDeniedException;
import BookPick.mvp.domain.curation.exception.common.CurationNotFoundException;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.repository.like.CurationLikeRepository;
import BookPick.mvp.domain.curation.service.base.read.CurationReadService;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.domain.user.service.subscribe.CurationSubscribeService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("큐레이션 조회 서비스 테스트")
class CurationReadServiceTest {

    @InjectMocks private CurationReadService curationReadService;

    @Mock private CurationRepository curationRepository;

    @Mock private CurationLikeRepository curationLikeRepository;

    @Mock private UserRepository userRepository;

    @Mock private CurationSubscribeService curationSubscribeService;

    @Mock private HttpServletRequest request;

    @Test
    @DisplayName("큐레이션 조회 성공 - 비로그인 사용자")
    void findCuration_success_notLoggedIn() {
        // given
        Long curationId = 1L;
        User mockUser = User.builder().id(1L).email("test@test.com").nickname("테스터").build();

        Curation mockCuration =
                Curation.builder()
                        .id(curationId)
                        .user(mockUser)
                        .title("테스트 큐레이션")
                        .isDrafted(false)
                        .viewCount(0)
                        .likeCount(0)
                        .commentCount(0)
                        .moods(List.of("감동적인"))
                        .genres(List.of("소설"))
                        .keywords(List.of("사랑"))
                        .styles(List.of("감성적인"))
                        .build();

        when(curationRepository.findByIdWithUserAndLock(curationId))
                .thenReturn(Optional.of(mockCuration));

        // when
        CurationGetRes result = curationReadService.findCuration(curationId, null, request, false);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(curationId);
        assertThat(result.title()).isEqualTo("테스트 큐레이션");
        assertThat(result.viewCount()).isEqualTo(1); // 조회수 증가 확인
        assertThat(result.isLiked()).isFalse();
        assertThat(result.subscribed()).isFalse();

        verify(curationRepository).findByIdWithUserAndLock(curationId);
        verify(curationLikeRepository, never()).findByUserIdAndCurationId(any(), any());
    }

    @Test
    @DisplayName("큐레이션 조회 성공 - 로그인 사용자, 좋아요 O, 구독 O")
    void findCuration_success_loggedIn_likedAndSubscribed() {
        // given
        Long curationId = 1L;
        Long userId = 2L;

        User curator = User.builder().id(1L).email("curator@test.com").nickname("큐레이터").build();

        User viewer = User.builder().id(userId).email("viewer@test.com").nickname("뷰어").build();

        Curation mockCuration =
                Curation.builder()
                        .id(curationId)
                        .user(curator)
                        .title("테스트 큐레이션")
                        .isDrafted(false)
                        .viewCount(5)
                        .likeCount(10)
                        .commentCount(3)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        CurationLike mockLike = new CurationLike();

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);

        when(curationRepository.findByIdWithUserAndLock(curationId))
                .thenReturn(Optional.of(mockCuration));
        when(curationLikeRepository.findByUserIdAndCurationId(userId, curationId))
                .thenReturn(Optional.of(mockLike));
        when(curationSubscribeService.isSubscribeCurator(userId, curator.getId())).thenReturn(true);

        // when
        CurationGetRes result =
                curationReadService.findCuration(curationId, userDetails, request, false);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(curationId);
        assertThat(result.viewCount()).isEqualTo(6); // 조회수 증가 확인
        assertThat(result.isLiked()).isTrue(); // 좋아요 확인
        assertThat(result.subscribed()).isTrue(); // 구독 확인

        verify(curationRepository).findByIdWithUserAndLock(curationId);
        verify(curationLikeRepository).findByUserIdAndCurationId(userId, curationId);
        verify(curationSubscribeService).isSubscribeCurator(userId, curator.getId());
    }

    @Test
    @DisplayName("큐레이션 조회 성공 - 로그인 사용자, 좋아요 X, 구독 X")
    void findCuration_success_loggedIn_notLikedAndNotSubscribed() {
        // given
        Long curationId = 1L;
        Long userId = 2L;

        User curator = User.builder().id(1L).email("curator@test.com").build();

        Curation mockCuration =
                Curation.builder()
                        .id(curationId)
                        .user(curator)
                        .title("테스트 큐레이션")
                        .isDrafted(false)
                        .viewCount(0)
                        .likeCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);

        when(curationRepository.findByIdWithUserAndLock(curationId))
                .thenReturn(Optional.of(mockCuration));
        when(curationLikeRepository.findByUserIdAndCurationId(userId, curationId))
                .thenReturn(Optional.empty());
        when(curationSubscribeService.isSubscribeCurator(userId, curator.getId()))
                .thenReturn(false);

        // when
        CurationGetRes result =
                curationReadService.findCuration(curationId, userDetails, request, false);

        // then
        assertThat(result.isLiked()).isFalse();
        assertThat(result.subscribed()).isFalse();

        verify(curationLikeRepository).findByUserIdAndCurationId(userId, curationId);
        verify(curationSubscribeService).isSubscribeCurator(userId, curator.getId());
    }

    @Test
    @DisplayName("큐레이션 수정용 조회 성공 - 작성자")
    void findCuration_editMode_success_owner() {
        // given
        Long curationId = 1L;
        Long userId = 1L;

        User owner = User.builder().id(userId).email("owner@test.com").build();

        Curation mockCuration =
                Curation.builder()
                        .id(curationId)
                        .user(owner)
                        .title("테스트 큐레이션")
                        .isDrafted(true)
                        .viewCount(0)
                        .likeCount(0)
                        .commentCount(0)
                        .bookTitle("책 제목")
                        .bookAuthor("작가")
                        .bookIsbn("1234567890")
                        .bookImageUrl("book.jpg")
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);

        when(curationRepository.findByIdWithUserAndLock(curationId))
                .thenReturn(Optional.of(mockCuration));
        when(curationLikeRepository.findByUserIdAndCurationId(userId, curationId))
                .thenReturn(Optional.empty());
        when(curationSubscribeService.isSubscribeCurator(userId, owner.getId())).thenReturn(false);

        // when
        CurationGetRes result =
                curationReadService.findCuration(curationId, userDetails, request, true);

        // then
        assertThat(result).isNotNull();
        assertThat(result.book()).isNotNull(); // 작성자는 책 정보 포함
        assertThat(result.book().title()).isEqualTo("책 제목");

        verify(curationRepository).findByIdWithUserAndLock(curationId);
    }

    @Test
    @DisplayName("큐레이션 수정용 조회 실패 - 작성자가 아님")
    void findCuration_editMode_fail_notOwner() {
        // given
        Long curationId = 1L;
        Long ownerId = 1L;
        Long viewerId = 2L;

        User owner = User.builder().id(ownerId).email("owner@test.com").build();

        Curation mockCuration =
                Curation.builder()
                        .id(curationId)
                        .user(owner)
                        .title("테스트 큐레이션")
                        .isDrafted(false)
                        .viewCount(0)
                        .likeCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(viewerId);

        when(curationRepository.findByIdWithUserAndLock(curationId))
                .thenReturn(Optional.of(mockCuration));
        when(curationLikeRepository.findByUserIdAndCurationId(viewerId, curationId))
                .thenReturn(Optional.empty());
        when(curationSubscribeService.isSubscribeCurator(viewerId, ownerId)).thenReturn(false);

        // when & then
        assertThatThrownBy(
                        () ->
                                curationReadService.findCuration(
                                        curationId, userDetails, request, true))
                .isInstanceOf(CurationAccessDeniedException.class);

        verify(curationRepository).findByIdWithUserAndLock(curationId);
    }

    @Test
    @DisplayName("존재하지 않는 큐레이션 조회 - 예외 발생")
    void findCuration_notFound() {
        // given
        Long curationId = 999L;

        when(curationRepository.findByIdWithUserAndLock(curationId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> curationReadService.findCuration(curationId, null, request, false))
                .isInstanceOf(CurationNotFoundException.class);

        verify(curationRepository).findByIdWithUserAndLock(curationId);
        verify(curationLikeRepository, never()).findByUserIdAndCurationId(any(), any());
    }
}
