package BookPick.mvp.domain.curation.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import BookPick.mvp.domain.curation.dto.base.CurationReq;
import BookPick.mvp.domain.curation.dto.base.create.CurationCreateResult;
import BookPick.mvp.domain.curation.dto.base.create.ETC.BookDto;
import BookPick.mvp.domain.curation.dto.base.create.ETC.RecommendDto;
import BookPick.mvp.domain.curation.dto.base.create.ETC.ThumbnailDto;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.service.base.create.CurationCreateService;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
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
@DisplayName("큐레이션 생성 서비스 테스트")
class CurationCreateServiceTest {

    @InjectMocks private CurationCreateService curationCreateService;

    @Mock private CurationRepository curationRepository;

    @Mock private UserRepository userRepository;

    @Test
    @DisplayName("큐레이션 발행 성공")
    void publishCuration_success() {
        // given
        Long userId = 1L;
        User mockUser = User.builder().id(userId).email("test@test.com").build();

        CurationReq req =
                new CurationReq(
                        "테스트 큐레이션",
                        new ThumbnailDto("thumbnail.jpg", "#FFFFFF"),
                        new BookDto("책 제목", "작가", "1234567890", "book.jpg"),
                        "이 책은 정말 좋습니다.",
                        new RecommendDto(
                                List.of("감동적인"), List.of("소설"), List.of("사랑"), List.of("감성적인")),
                        false // isDrafted = false (발행)
                        );

        Curation mockCuration =
                Curation.builder()
                        .id(1L)
                        .user(mockUser)
                        .title(req.title())
                        .isDrafted(false)
                        .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(curationRepository.save(any(Curation.class))).thenReturn(mockCuration);

        // when
        CurationCreateResult result = curationCreateService.saveCuration(userId, req);

        // then
        assertThat(result.curationCreateRes().id()).isEqualTo(1L);
        assertThat(result.successCode()).isEqualTo(SuccessCode.CURATION_PUBLISH_SUCCESS);

        verify(userRepository).findById(userId);
        verify(curationRepository).save(argThat(c -> !c.getIsDrafted()));
    }

    @Test
    @DisplayName("큐레이션 임시저장 성공")
    void draftCuration_success() {
        // given
        Long userId = 1L;
        User mockUser = User.builder().id(userId).email("test@test.com").build();

        CurationReq req =
                new CurationReq(
                        "임시저장 큐레이션",
                        new ThumbnailDto("thumbnail.jpg", "#FFFFFF"),
                        new BookDto("책 제목", "작가", "1234567890", "book.jpg"),
                        "리뷰 작성 중...",
                        new RecommendDto(
                                List.of("감동적인"), List.of("소설"), List.of("사랑"), List.of("감성적인")),
                        true // isDrafted = true (임시저장)
                        );

        Curation mockCuration =
                Curation.builder().id(2L).user(mockUser).title(req.title()).isDrafted(true).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(curationRepository.save(any(Curation.class))).thenReturn(mockCuration);

        // when
        CurationCreateResult result = curationCreateService.saveCuration(userId, req);

        // then
        assertThat(result.curationCreateRes().id()).isEqualTo(2L);
        assertThat(result.successCode()).isEqualTo(SuccessCode.CURATION_DRAFT_SUCCESS);

        verify(userRepository).findById(userId);
        verify(curationRepository).save(argThat(c -> c.getIsDrafted()));
    }

    @Test
    @DisplayName("존재하지 않는 유저 - 예외 발생")
    void createCuration_userNotFound() {
        // given
        Long userId = 999L;
        CurationReq req =
                new CurationReq(
                        "테스트",
                        new ThumbnailDto("thumb.jpg", "#FFF"),
                        new BookDto("책", "작가", "123", "book.jpg"),
                        "리뷰",
                        new RecommendDto(List.of(), List.of(), List.of(), List.of()),
                        false);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> curationCreateService.saveCuration(userId, req))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(userId);
        verify(curationRepository, never()).save(any(Curation.class));
    }

    @Test
    @DisplayName("발행/임시저장 구분 확인")
    void createCuration_publishVsDraft() {
        // given
        Long userId = 1L;
        User mockUser = User.builder().id(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(curationRepository.save(any(Curation.class)))
                .thenAnswer(
                        invocation -> {
                            Curation c = invocation.getArgument(0);
                            return Curation.builder()
                                    .id(1L)
                                    .user(mockUser)
                                    .isDrafted(c.getIsDrafted())
                                    .build();
                        });

        // when - 발행
        CurationReq publishReq =
                new CurationReq(
                        "발행",
                        new ThumbnailDto("t.jpg", "#FFF"),
                        new BookDto("책", "작가", "123", "b.jpg"),
                        "리뷰",
                        new RecommendDto(List.of(), List.of(), List.of(), List.of()),
                        false);
        CurationCreateResult publishResult = curationCreateService.saveCuration(userId, publishReq);

        // when - 임시저장
        CurationReq draftReq =
                new CurationReq(
                        "임시",
                        new ThumbnailDto("t.jpg", "#FFF"),
                        new BookDto("책", "작가", "123", "b.jpg"),
                        "리뷰",
                        new RecommendDto(List.of(), List.of(), List.of(), List.of()),
                        true);
        CurationCreateResult draftResult = curationCreateService.saveCuration(userId, draftReq);

        // then
        assertThat(publishResult.successCode()).isEqualTo(SuccessCode.CURATION_PUBLISH_SUCCESS);
        assertThat(draftResult.successCode()).isEqualTo(SuccessCode.CURATION_DRAFT_SUCCESS);

        verify(curationRepository, times(2)).save(any(Curation.class));
    }
}
