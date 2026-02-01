package BookPick.mvp.domain.curation.util.list.similarity;

import static org.assertj.core.api.Assertions.assertThat;

import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.entity.Curation;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SimilarityMatcher 테스트")
class SimilarityMatcherTest {

    @Test
    @DisplayName("모든 카테고리 다중 매칭 시 최대 점수")
    void calculate_allMultiMatch_maxScore() {
        // given
        Curation curation = Curation.builder()
                .genres(List.of("소설", "에세이"))
                .keywords(List.of("위로", "성장"))
                .moods(List.of("퇴근 후", "카페"))
                .styles(List.of("몰입형", "정독형"))
                .build();

        ReadingPreferenceInfo preference = new ReadingPreferenceInfo(
                1L,
                "INFP",
                Set.of(),
                Set.of(),
                List.of(),
                List.of("퇴근 후", "카페", "비 오는 날"),
                List.of("소설", "에세이", "역사"),
                List.of("위로", "성장", "사랑"),
                List.of("몰입형", "정독형", "속독형")
        );

        // when
        int score = SimilarityMatcher.calculate(curation, preference);

        // then
        // 기본 30 + 장르 25 + 키워드 20 + 분위기 15 + 스타일 10 = 100
        assertThat(score).isEqualTo(100);
    }

    @Test
    @DisplayName("각 카테고리 단일 매칭 시 점수")
    void calculate_allSingleMatch() {
        // given
        Curation curation = Curation.builder()
                .genres(List.of("소설"))
                .keywords(List.of("위로"))
                .moods(List.of("퇴근 후"))
                .styles(List.of("몰입형"))
                .build();

        ReadingPreferenceInfo preference = new ReadingPreferenceInfo(
                1L,
                "INFP",
                Set.of(),
                Set.of(),
                List.of(),
                List.of("퇴근 후"),
                List.of("소설"),
                List.of("위로"),
                List.of("몰입형")
        );

        // when
        int score = SimilarityMatcher.calculate(curation, preference);

        // then
        // 기본 30 + 장르 15 + 키워드 12 + 분위기 9 + 스타일 6 = 72
        assertThat(score).isEqualTo(72);
    }

    @Test
    @DisplayName("매칭 없을 시 기본 점수만")
    void calculate_noMatch_baseScore() {
        // given
        Curation curation = Curation.builder()
                .genres(List.of("소설"))
                .keywords(List.of("위로"))
                .moods(List.of("퇴근 후"))
                .styles(List.of("몰입형"))
                .build();

        ReadingPreferenceInfo preference = new ReadingPreferenceInfo(
                1L,
                "INFP",
                Set.of(),
                Set.of(),
                List.of(),
                List.of("카페"),
                List.of("역사"),
                List.of("사랑"),
                List.of("속독형")
        );

        // when
        int score = SimilarityMatcher.calculate(curation, preference);

        // then
        assertThat(score).isEqualTo(30);
    }

    @Test
    @DisplayName("장르만 다중 매칭")
    void calculate_onlyGenreMultiMatch() {
        // given
        Curation curation = Curation.builder()
                .genres(List.of("소설", "에세이", "역사"))
                .keywords(List.of("공포"))
                .moods(List.of("새벽 시간"))
                .styles(List.of("비평적"))
                .build();

        ReadingPreferenceInfo preference = new ReadingPreferenceInfo(
                1L,
                "INFP",
                Set.of(),
                Set.of(),
                List.of(),
                List.of("퇴근 후"),
                List.of("소설", "에세이"),
                List.of("위로"),
                List.of("몰입형")
        );

        // when
        int score = SimilarityMatcher.calculate(curation, preference);

        // then
        // 기본 30 + 장르 25 = 55
        assertThat(score).isEqualTo(55);
    }

    @Test
    @DisplayName("매칭 요약 문자열 생성")
    void getMatchedSummary() {
        // given
        Curation curation = Curation.builder()
                .genres(List.of("소설", "에세이"))
                .keywords(List.of("위로"))
                .moods(List.of("퇴근 후"))
                .styles(List.of("몰입형"))
                .build();

        ReadingPreferenceInfo preference = new ReadingPreferenceInfo(
                1L,
                "INFP",
                Set.of(),
                Set.of(),
                List.of(),
                List.of("퇴근 후"),
                List.of("소설"),
                List.of("위로"),
                List.of("몰입형")
        );

        // when
        String summary = SimilarityMatcher.getMatchedSummary(curation, preference);

        // then
        assertThat(summary).contains("소설");
        assertThat(summary).contains("위로");
        assertThat(summary).contains("퇴근 후");
        assertThat(summary).contains("몰입형");
    }
}
