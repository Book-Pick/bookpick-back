package BookPick.mvp.domain.curation.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import BookPick.mvp.domain.curation.dto.base.create.ETC.BookDto;
import BookPick.mvp.domain.curation.dto.base.create.ETC.RecommendDto;
import BookPick.mvp.domain.curation.dto.base.create.ETC.ThumbnailDto;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateReq;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateResult;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.exception.common.CurationAccessDeniedException;
import BookPick.mvp.domain.curation.exception.common.CurationAlreadyPublishedException;
import BookPick.mvp.domain.curation.exception.common.CurationNotFoundException;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.repository.like.CurationLikeRepository;
import BookPick.mvp.domain.curation.service.base.update.CurationUpdateService;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.domain.user.service.subscribe.CurationSubscribeService;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("큐레이션 수정 서비스 테스트")
class CurationUpdateServiceTest {

    @InjectMocks private CurationUpdateService curationUpdateService;

    @Mock private CurationRepository curationRepository;

    @Mock private CurationLikeRepository curationLikeRepository;

    @Mock private UserRepository userRepository;

    @Mock private CurationSubscribeService curationSubscribeService;

    @Test
    @DisplayName("임시저장 -> 임시저장 수정 성공")
    void updateCuration_draftToDraft_success() {
        // given
        Long userId = 1L;
        Long curationId = 1L;

        User mockUser = User.builder().id(userId).email("test@test.com").build();

        Curation mockCuration =
                Curation.builder()
                        .id(curationId)
                        .user(mockUser)
                        .title("기존 제목")
                        .isDrafted(true)
                        .likeCount(0)
                        .viewCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        CurationUpdateReq req =
                new CurationUpdateReq(
                        "수정된 제목",
                        new ThumbnailDto("new-thumb.jpg", "#000000"),
                        new BookDto("새 책", "새 작가", "9876543210", "new-book.jpg"),
                        "수정된 리뷰",
                        new RecommendDto(
                                List.of("슬픈"), List.of("에세이"), List.of("이별"), List.of("담백한")),
                        true // isDrafted = true
                        );

        when(curationRepository.findById(curationId)).thenReturn(Optional.of(mockCuration));

        // when
        CurationUpdateResult result = curationUpdateService.updateCuration(userId, curationId, req);

        // then
        assertThat(result).isNotNull();
        assertThat(result.successCode()).isEqualTo(SuccessCode.CURATION_DRAFT_UPDATE_SUCCESS);
        assertThat(result.curationUpdateRes().id()).isEqualTo(curationId);
        assertThat(mockCuration.getTitle()).isEqualTo("수정된 제목");
        assertThat(mockCuration.getIsDrafted()).isTrue();

        verify(curationRepository).findById(curationId);
    }

    @Test
    @DisplayName("임시저장 -> 발행 성공")
    void updateCuration_draftToPublished_success() {
        // given
        Long userId = 1L;
        Long curationId = 1L;

        User mockUser = User.builder().id(userId).email("test@test.com").build();

        Curation mockCuration =
                Curation.builder()
                        .id(curationId)
                        .user(mockUser)
                        .title("임시저장 제목")
                        .isDrafted(true)
                        .likeCount(0)
                        .viewCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        CurationUpdateReq req =
                new CurationUpdateReq(
                        "발행할 제목",
                        new ThumbnailDto("thumb.jpg", "#FFFFFF"),
                        new BookDto("책", "작가", "123", "book.jpg"),
                        "리뷰",
                        new RecommendDto(List.of(), List.of(), List.of(), List.of()),
                        false // isDrafted = false (발행)
                        );

        when(curationRepository.findById(curationId)).thenReturn(Optional.of(mockCuration));

        // when
        CurationUpdateResult result = curationUpdateService.updateCuration(userId, curationId, req);

        // then
        assertThat(result).isNotNull();
        assertThat(result.successCode()).isEqualTo(SuccessCode.DRAFTED_CURATION_PUBLISH_SUCCESS);
        assertThat(mockCuration.getIsDrafted()).isFalse();
        assertThat(mockCuration.getPublishedAt()).isNotNull();

        verify(curationRepository).findById(curationId);
    }

    @Test
    @DisplayName("발행본 -> 발행본 수정 성공")
    void updateCuration_publishedToPublished_success() {
        // given
        Long userId = 1L;
        Long curationId = 1L;

        User mockUser = User.builder().id(userId).email("test@test.com").build();

        Curation mockCuration =
                Curation.builder()
                        .id(curationId)
                        .user(mockUser)
                        .title("발행된 제목")
                        .isDrafted(false)
                        .likeCount(5)
                        .viewCount(100)
                        .commentCount(10)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        CurationUpdateReq req =
                new CurationUpdateReq(
                        "수정된 발행 제목",
                        new ThumbnailDto("thumb.jpg", "#FFFFFF"),
                        new BookDto("책", "작가", "123", "book.jpg"),
                        "수정된 리뷰",
                        new RecommendDto(List.of(), List.of(), List.of(), List.of()),
                        false // isDrafted = false
                        );

        when(curationRepository.findById(curationId)).thenReturn(Optional.of(mockCuration));

        // when
        CurationUpdateResult result = curationUpdateService.updateCuration(userId, curationId, req);

        // then
        assertThat(result).isNotNull();
        assertThat(result.successCode()).isEqualTo(SuccessCode.CURATION_UPDATE_SUCCESS);
        assertThat(mockCuration.getTitle()).isEqualTo("수정된 발행 제목");
        assertThat(mockCuration.getIsDrafted()).isFalse();

        verify(curationRepository).findById(curationId);
    }

    @Test
    @DisplayName("발행본 -> 임시저장 변경 시도 - 예외 발생")
    void updateCuration_publishedToDraft_fail() {
        // given
        Long userId = 1L;
        Long curationId = 1L;

        User mockUser = User.builder().id(userId).email("test@test.com").build();

        Curation mockCuration =
                Curation.builder()
                        .id(curationId)
                        .user(mockUser)
                        .title("발행된 제목")
                        .isDrafted(false)
                        .likeCount(0)
                        .viewCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        CurationUpdateReq req =
                new CurationUpdateReq(
                        "제목",
                        new ThumbnailDto("thumb.jpg", "#FFFFFF"),
                        new BookDto("책", "작가", "123", "book.jpg"),
                        "리뷰",
                        new RecommendDto(List.of(), List.of(), List.of(), List.of()),
                        true // isDrafted = true (임시저장으로 변경 시도)
                        );

        when(curationRepository.findById(curationId)).thenReturn(Optional.of(mockCuration));

        // when & then
        assertThatThrownBy(() -> curationUpdateService.updateCuration(userId, curationId, req))
                .isInstanceOf(CurationAlreadyPublishedException.class);

        verify(curationRepository).findById(curationId);
    }

    @Test
    @DisplayName("다른 사용자가 수정 시도 - 예외 발생")
    void updateCuration_notOwner_fail() {
        // given
        Long ownerId = 1L;
        Long attackerId = 2L;
        Long curationId = 1L;

        User owner = User.builder().id(ownerId).email("owner@test.com").build();

        Curation mockCuration =
                Curation.builder()
                        .id(curationId)
                        .user(owner)
                        .title("제목")
                        .isDrafted(true)
                        .likeCount(0)
                        .viewCount(0)
                        .commentCount(0)
                        .moods(List.of())
                        .genres(List.of())
                        .keywords(List.of())
                        .styles(List.of())
                        .build();

        CurationUpdateReq req =
                new CurationUpdateReq(
                        "해킹 시도",
                        new ThumbnailDto("thumb.jpg", "#FFFFFF"),
                        new BookDto("책", "작가", "123", "book.jpg"),
                        "리뷰",
                        new RecommendDto(List.of(), List.of(), List.of(), List.of()),
                        true);

        when(curationRepository.findById(curationId)).thenReturn(Optional.of(mockCuration));

        // when & then
        assertThatThrownBy(() -> curationUpdateService.updateCuration(attackerId, curationId, req))
                .isInstanceOf(CurationAccessDeniedException.class);

        verify(curationRepository).findById(curationId);
    }

    @Test
    @DisplayName("존재하지 않는 큐레이션 수정 - 예외 발생")
    void updateCuration_notFound_fail() {
        // given
        Long userId = 1L;
        Long curationId = 999L;

        CurationUpdateReq req =
                new CurationUpdateReq(
                        "제목",
                        new ThumbnailDto("thumb.jpg", "#FFFFFF"),
                        new BookDto("책", "작가", "123", "book.jpg"),
                        "리뷰",
                        new RecommendDto(List.of(), List.of(), List.of(), List.of()),
                        false);

        when(curationRepository.findById(curationId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> curationUpdateService.updateCuration(userId, curationId, req))
                .isInstanceOf(CurationNotFoundException.class);

        verify(curationRepository).findById(curationId);
    }

    @Test
    @DisplayName("큐레이션 업데이트 시 내용 변경 확인")
    void updateCuration_contentChange_success() {
        // given
        Long userId = 1L;
        Long curationId = 1L;

        User mockUser = User.builder().id(userId).build();

        Curation mockCuration =
                Curation.builder()
                        .id(curationId)
                        .user(mockUser)
                        .title("기존 제목")
                        .thumbnailUrl("old-thumb.jpg")
                        .thumbnailColor("#FFFFFF")
                        .bookTitle("기존 책")
                        .bookAuthor("기존 작가")
                        .bookIsbn("111")
                        .bookImageUrl("old-book.jpg")
                        .review("기존 리뷰")
                        .isDrafted(false)
                        .likeCount(0)
                        .viewCount(0)
                        .commentCount(0)
                        .moods(List.of("기쁜"))
                        .genres(List.of("소설"))
                        .keywords(List.of("사랑"))
                        .styles(List.of("감성적인"))
                        .build();

        CurationUpdateReq req =
                new CurationUpdateReq(
                        "새 제목",
                        new ThumbnailDto("new-thumb.jpg", "#000000"),
                        new BookDto("새 책", "새 작가", "222", "new-book.jpg"),
                        "새 리뷰",
                        new RecommendDto(
                                List.of("슬픈"), List.of("에세이"), List.of("이별"), List.of("담백한")),
                        false);

        when(curationRepository.findById(curationId)).thenReturn(Optional.of(mockCuration));

        // when
        CurationUpdateResult result = curationUpdateService.updateCuration(userId, curationId, req);

        // then
        assertThat(mockCuration.getTitle()).isEqualTo("새 제목");
        assertThat(mockCuration.getThumbnailUrl()).isEqualTo("new-thumb.jpg");
        assertThat(mockCuration.getThumbnailColor()).isEqualTo("#000000");
        assertThat(mockCuration.getBookTitle()).isEqualTo("새 책");
        assertThat(mockCuration.getBookAuthor()).isEqualTo("새 작가");
        assertThat(mockCuration.getBookIsbn()).isEqualTo("222");
        assertThat(mockCuration.getBookImageUrl()).isEqualTo("new-book.jpg");
        assertThat(mockCuration.getReview()).isEqualTo("새 리뷰");
        assertThat(mockCuration.getMoods()).containsExactly("슬픈");
        assertThat(mockCuration.getGenres()).containsExactly("에세이");
        assertThat(mockCuration.getKeywords()).containsExactly("이별");
        assertThat(mockCuration.getStyles()).containsExactly("담백한");

        verify(curationRepository).findById(curationId);
    }
}
