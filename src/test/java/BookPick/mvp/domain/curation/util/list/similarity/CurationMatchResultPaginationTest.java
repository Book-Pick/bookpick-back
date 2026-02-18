package BookPick.mvp.domain.curation.util.list.similarity;

import static org.assertj.core.api.Assertions.assertThat;

import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.util.gemini.dto.CurationMatchResult;
import BookPick.mvp.domain.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayName("CurationMatchResultPagination 테스트")
class CurationMatchResultPaginationTest {

    @Test
    @DisplayName("커서 기반 페이지네이션")
    void paginate_withCursor() {
        // given
        User dummyUser =
                User.builder().id(99L).build(); // Dummy user for CurationMatchResult builder
        List<CurationMatchResult> results =
                IntStream.range(0, 20)
                        .mapToObj(
                                i ->
                                        CurationMatchResult.builder()
                                                .curation(Curation.builder().id((long) i).build())
                                                .user(dummyUser)
                                                .totalMatchCount(i)
                                                .matchedMood("mood")
                                                .matchedGenre("genre")
                                                .matchedKeyword("keyword")
                                                .matchedStyle("style")
                                                .matched("matched")
                                                .build())
                        .collect(Collectors.toList());
        Pageable pageable = PageRequest.of(0, 5);
        Long cursor = 10L;

        // when
        List<CurationMatchResult> paginated =
                CurationMatchResultPagination.paginate(results, cursor, pageable);

        // then
        assertThat(paginated).hasSize(5);
        assertThat(paginated.get(0).getCuration().getId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("커서가 null인 경우")
    void paginate_nullCursor() {
        // given
        User dummyUser =
                User.builder().id(99L).build(); // Dummy user for CurationMatchResult builder
        List<CurationMatchResult> results =
                IntStream.range(0, 20)
                        .mapToObj(
                                i ->
                                        CurationMatchResult.builder()
                                                .curation(Curation.builder().id((long) i).build())
                                                .user(dummyUser)
                                                .totalMatchCount(i)
                                                .matchedMood("mood")
                                                .matchedGenre("genre")
                                                .matchedKeyword("keyword")
                                                .matchedStyle("style")
                                                .matched("matched")
                                                .build())
                        .collect(Collectors.toList());
        Pageable pageable = PageRequest.of(0, 5);

        // when
        List<CurationMatchResult> paginated =
                CurationMatchResultPagination.paginate(results, null, pageable);

        // then
        assertThat(paginated).hasSize(5);
        assertThat(paginated.get(0).getCuration().getId()).isEqualTo(0L);
    }

    @Test
    @DisplayName("커서가 범위를 벗어나는 경우")
    void paginate_cursorOutOfBounds() {
        // given
        User dummyUser =
                User.builder().id(99L).build(); // Dummy user for CurationMatchResult builder
        List<CurationMatchResult> results =
                IntStream.range(0, 10)
                        .mapToObj(
                                i ->
                                        CurationMatchResult.builder()
                                                .curation(Curation.builder().id((long) i).build())
                                                .user(dummyUser)
                                                .totalMatchCount(i)
                                                .matchedMood("mood")
                                                .matchedGenre("genre")
                                                .matchedKeyword("keyword")
                                                .matchedStyle("style")
                                                .matched("matched")
                                                .build())
                        .collect(Collectors.toList());
        Pageable pageable = PageRequest.of(0, 5);
        Long cursor = 15L;

        // when
        List<CurationMatchResult> paginated =
                CurationMatchResultPagination.paginate(results, cursor, pageable);

        // then
        assertThat(paginated).isEmpty();
    }
}
