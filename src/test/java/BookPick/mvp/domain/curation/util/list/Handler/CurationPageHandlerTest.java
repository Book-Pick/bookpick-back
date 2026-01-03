package BookPick.mvp.domain.curation.util.list.Handler;

import BookPick.mvp.domain.curation.dto.base.get.list.CurationContentRes;
import BookPick.mvp.domain.curation.dto.base.get.list.CursorPage;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.util.list.fetcher.CurationFetcher;
import BookPick.mvp.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CurationPageHandler 테스트")
class CurationPageHandlerTest {

    @InjectMocks
    private CurationPageHandler curationPageHandler;

    @Mock
    private CurationFetcher curationFetcher;

    @Test
    @DisplayName("다음 페이지가 있는 경우 커서 페이지 생성")
    void createCursorPage_withNextPage() {
        // given
        List<Curation> curations = IntStream.range(0, 11)
                .mapToObj(i -> Curation.builder().id((long) i).build())
                .collect(Collectors.toList());
        int size = 10;
        when(curationFetcher.calculateNextCursor(curations, size, true)).thenReturn(10L);

        // when
        CursorPage<Curation> cursorPage = curationPageHandler.createCursorPage(curations, size);

        // then
        assertThat(cursorPage.isHasNext()).isTrue();
        assertThat(cursorPage.getContent()).hasSize(10);
        assertThat(cursorPage.getNextCursor()).isEqualTo(10L);
    }

    @Test
    @DisplayName("다음 페이지가 없는 경우 커서 페이지 생성")
    void createCursorPage_withoutNextPage() {
        // given
        List<Curation> curations = IntStream.range(0, 5)
                .mapToObj(i -> Curation.builder().id((long) i).build())
                .collect(Collectors.toList());
        int size = 10;
        when(curationFetcher.calculateNextCursor(curations, size, false)).thenReturn(null);

        // when
        CursorPage<Curation> cursorPage = curationPageHandler.createCursorPage(curations, size);

        // then
        assertThat(cursorPage.isHasNext()).isFalse();
        assertThat(cursorPage.getContent()).hasSize(5);
        assertThat(cursorPage.getNextCursor()).isNull();
    }

    @Test
    @DisplayName("큐레이션 리스트를 DTO로 변환")
    void convertToContentRes_conversion() {
        // given
        User user = User.builder().id(1L).build();
        Curation c1 = Curation.builder().id(1L).user(user).title("title1").build();
        Curation c2 = Curation.builder().id(2L).user(user).title("title2").build();
        Curation c3 = Curation.builder().id(3L).user(user).title("title3").build();
        List<Curation> curations = List.of(c1, c2, c3);
        Set<Long> likedIds = Set.of(1L, 3L);

        // when
        List<CurationContentRes> res = curationPageHandler.convertToContentRes(curations, likedIds);

        // then
        assertThat(res).hasSize(3);
        assertThat(res.get(0).isLiked()).isTrue();
        assertThat(res.get(1).isLiked()).isFalse();
        assertThat(res.get(2).isLiked()).isTrue();
    }
}
