package BookPick.mvp.domain.ReadingPreference.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import BookPick.mvp.domain.ReadingPreference.Exception.fail.AlreadyRegisteredReadingPreferenceException;
import BookPick.mvp.domain.ReadingPreference.Exception.fail.UserReadingPreferenceNotExisted;
import BookPick.mvp.domain.ReadingPreference.dto.ETC.Delete.ReadingPreferenceDeleteRes;
import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceReq;
import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceRes;
import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;
import BookPick.mvp.domain.ReadingPreference.repository.ReadingPreferenceRepository;
import BookPick.mvp.domain.author.dto.preference.AuthorDto;
import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.author.service.AuthorSaveService;
import BookPick.mvp.domain.book.dto.preference.BookDto;
import BookPick.mvp.domain.book.entity.Book;
import BookPick.mvp.domain.book.repository.BookRepository;
import BookPick.mvp.domain.book.service.BookSaveService;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("독서취향 서비스 테스트")
class ReadingPreferenceServiceTest {

    @InjectMocks private ReadingPreferenceService readingPreferenceService;

    @Mock private ReadingPreferenceRepository readingPreferenceRepository;

    @Mock private UserRepository userRepository;

    @Mock private BookSaveService bookSaveService;

    @Mock private AuthorSaveService authorSaveService;

    @Mock private BookRepository bookRepository;

    @Mock private ReadingPreferenceValidCheckService readingPreferenceValidCheckService;

    @Test
    @DisplayName("정상 독서취향 등록 성공")
    void addReadingPreference_success() {
        // given
        Long userId = 1L;
        User mockUser =
                User.builder().id(userId).email("test@test.com").password("password").build();

        BookDto bookDto = new BookDto("Test Book", "Test Author", "image.jpg", "isbn123");
        AuthorDto authorDto = new AuthorDto("Test Author");

        ReadingPreferenceReq req =
                new ReadingPreferenceReq(
                        "INTJ",
                        Set.of(bookDto),
                        Set.of(authorDto),
                        List.of("차분한"),
                        List.of("매일 읽기"),
                        List.of("소설"),
                        List.of("성장"),
                        List.of("몰입형"));

        Book mockBook = Book.builder().isbn("isbn123").title("Test Book").build();

        Author mockAuthor = Author.builder().name("Test Author").build();

        ReadingPreference mockPreference =
                ReadingPreference.builder()
                        .user(mockUser)
                        .mbti("INTJ")
                        .favoriteBooks(Set.of(mockBook))
                        .favoriteAuthors(Set.of(mockAuthor))
                        .moods(List.of("차분한"))
                        .readingHabits(List.of("매일 읽기"))
                        .genres(List.of("소설"))
                        .keywords(List.of("성장"))
                        .readingStyles(List.of("몰입형"))
                        .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(readingPreferenceRepository.existsByUserId(userId)).thenReturn(false);
        when(bookSaveService.saveBookIfNotExistsDto(anySet())).thenReturn(Set.of(mockBook));
        when(authorSaveService.saveAuthorIfNotExistsDto(anySet())).thenReturn(Set.of(mockAuthor));
        when(readingPreferenceRepository.save(any(ReadingPreference.class)))
                .thenReturn(mockPreference);

        // when
        ReadingPreferenceRes result = readingPreferenceService.addReadingPreference(userId, req);

        // then
        assertThat(result).isNotNull();
        assertThat(result.mbti()).isEqualTo("INTJ");
        verify(userRepository).findById(userId);
        verify(readingPreferenceRepository).existsByUserId(userId);
        verify(bookSaveService).saveBookIfNotExistsDto(anySet());
        verify(authorSaveService).saveAuthorIfNotExistsDto(anySet());
        verify(readingPreferenceRepository).save(any(ReadingPreference.class));
    }

    @Test
    @DisplayName("독서취향 등록 실패 - 사용자 없음")
    void addReadingPreference_fail_userNotFound() {
        // given
        Long userId = 1L;
        ReadingPreferenceReq req =
                new ReadingPreferenceReq(
                        "INTJ",
                        Set.of(),
                        Set.of(),
                        List.of("차분한"),
                        List.of("매일 읽기"),
                        List.of("소설"),
                        List.of("성장"),
                        List.of("몰입형"));

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> readingPreferenceService.addReadingPreference(userId, req))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(userId);
        verify(readingPreferenceRepository, never()).save(any());
    }

    @Test
    @DisplayName("독서취향 등록 실패 - 이미 등록됨")
    void addReadingPreference_fail_alreadyRegistered() {
        // given
        Long userId = 1L;
        User mockUser =
                User.builder().id(userId).email("test@test.com").password("password").build();

        ReadingPreferenceReq req =
                new ReadingPreferenceReq(
                        "INTJ",
                        Set.of(),
                        Set.of(),
                        List.of("차분한"),
                        List.of("매일 읽기"),
                        List.of("소설"),
                        List.of("성장"),
                        List.of("몰입형"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(readingPreferenceRepository.existsByUserId(userId)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> readingPreferenceService.addReadingPreference(userId, req))
                .isInstanceOf(AlreadyRegisteredReadingPreferenceException.class);

        verify(readingPreferenceRepository, never()).save(any());
    }

    @Test
    @DisplayName("빈 독서취향 등록 성공")
    void addClearReadingPreference_success() {
        // given
        Long userId = 1L;
        User mockUser =
                User.builder().id(userId).email("test@test.com").password("password").build();

        ReadingPreference mockPreference = ReadingPreference.clearPreferences(mockUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(readingPreferenceRepository.existsByUserId(userId)).thenReturn(false);
        when(readingPreferenceRepository.save(any(ReadingPreference.class)))
                .thenReturn(mockPreference);

        // when
        ReadingPreferenceRes result = readingPreferenceService.addClearReadingPreference(userId);

        // then
        assertThat(result).isNotNull();
        verify(userRepository).findById(userId);
        verify(readingPreferenceRepository).existsByUserId(userId);
        verify(readingPreferenceRepository).save(any(ReadingPreference.class));
    }

    @Test
    @DisplayName("독서취향 조회 성공")
    void findReadingPreference_success() {
        // given
        Long userId = 1L;
        User mockUser =
                User.builder().id(userId).email("test@test.com").password("password").build();

        ReadingPreference mockPreference =
                ReadingPreference.builder().user(mockUser).mbti("INTJ").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(readingPreferenceRepository.findByUserId(userId))
                .thenReturn(Optional.of(mockPreference));

        // when
        ReadingPreferenceRes result = readingPreferenceService.findReadingPreference(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.mbti()).isEqualTo("INTJ");
        verify(userRepository).findById(userId);
        verify(readingPreferenceRepository).findByUserId(userId);
    }

    @Test
    @DisplayName("독서취향 조회 실패 - 독서취향 없음")
    void findReadingPreference_fail_notExisted() {
        // given
        Long userId = 1L;
        User mockUser =
                User.builder().id(userId).email("test@test.com").password("password").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(readingPreferenceRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> readingPreferenceService.findReadingPreference(userId))
                .isInstanceOf(UserReadingPreferenceNotExisted.class);

        verify(userRepository).findById(userId);
        verify(readingPreferenceRepository).findByUserId(userId);
    }

    @Test
    @DisplayName("독서취향 수정 성공")
    void modifyReadingPreference_success() {
        // given
        Long userId = 1L;
        User mockUser =
                User.builder().id(userId).email("test@test.com").password("password").build();

        BookDto bookDto = new BookDto("New Book", "New Author", "new_image.jpg", "isbn456");
        AuthorDto authorDto = new AuthorDto("New Author");

        ReadingPreferenceReq req =
                new ReadingPreferenceReq(
                        "ENFP",
                        Set.of(bookDto),
                        Set.of(authorDto),
                        List.of("설레는"),
                        List.of("주말에 읽기"),
                        List.of("에세이"),
                        List.of("힐링"),
                        List.of("건너뛰며"));

        Book mockBook = Book.builder().isbn("isbn456").title("New Book").build();

        Author mockAuthor = Author.builder().name("New Author").build();

        ReadingPreference mockPreference =
                ReadingPreference.builder().user(mockUser).mbti("INTJ").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(readingPreferenceRepository.findByUserId(userId))
                .thenReturn(Optional.of(mockPreference));
        when(bookSaveService.saveBookIfNotExistsDto(anySet())).thenReturn(Set.of(mockBook));
        when(authorSaveService.saveAuthorIfNotExistsDto(anySet())).thenReturn(Set.of(mockAuthor));
        doNothing().when(readingPreferenceValidCheckService).validateReadingPreferenceReq(any());

        // when
        ReadingPreferenceRes result = readingPreferenceService.modifyReadingPreference(userId, req);

        // then
        assertThat(result).isNotNull();
        verify(userRepository).findById(userId);
        verify(readingPreferenceRepository).findByUserId(userId);
        verify(bookSaveService).saveBookIfNotExistsDto(anySet());
        verify(authorSaveService).saveAuthorIfNotExistsDto(anySet());
        verify(readingPreferenceValidCheckService).validateReadingPreferenceReq(req);
    }

    @Test
    @DisplayName("독서취향 삭제 성공")
    void removeReadingPreference_success() {
        // given
        Long userId = 1L;
        User mockUser =
                User.builder().id(userId).email("test@test.com").password("password").build();

        ReadingPreference mockPreference =
                ReadingPreference.builder().user(mockUser).mbti("INTJ").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(readingPreferenceRepository.findByUserId(userId))
                .thenReturn(Optional.of(mockPreference));
        doNothing().when(readingPreferenceRepository).delete(mockPreference);

        // when
        ReadingPreferenceDeleteRes result =
                readingPreferenceService.removeReadingPreference(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.deletedAt()).isNotNull();
        verify(userRepository).findById(userId);
        verify(readingPreferenceRepository).findByUserId(userId);
        verify(readingPreferenceRepository).delete(mockPreference);
    }

    @Test
    @DisplayName("독서취향 삭제 실패 - 독서취향 없음")
    void removeReadingPreference_fail_notExisted() {
        // given
        Long userId = 1L;
        User mockUser =
                User.builder().id(userId).email("test@test.com").password("password").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(readingPreferenceRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> readingPreferenceService.removeReadingPreference(userId))
                .isInstanceOf(UserReadingPreferenceNotExisted.class);

        verify(readingPreferenceRepository, never()).delete(any());
    }
}
