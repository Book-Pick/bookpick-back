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
    @DisplayName("각 카테고리 1:1 완벽 매칭 시 최대 점수")
    void calculate_allSingleMatch_perfectRatio() {
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
        // 각 카테고리 matchRatio=1/1=1.0 → 기본 30 + 장르 25 + 키워드 20 + 분위기 15 + 스타일 10 = 100
        assertThat(score).isEqualTo(100);
    }

    @Test
    @DisplayName("부분 매칭 시 매칭 비율에 따른 점진적 점수")
    void calculate_partialMatch_gradualScore() {
        // given
        Curation curation = Curation.builder()
                .genres(List.of("소설", "에세이", "역사", "철학"))
                .keywords(List.of("위로", "성장", "공감", "사랑"))
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
                List.of("위로", "성장"),
                List.of("몰입형")
        );

        // when
        int score = SimilarityMatcher.calculate(curation, preference);

        // then
        // 장르: 1/1=1.0 → 25*(0.5+0.5*1.0)=25
        // 키워드: 2/4 possibleMatches=min(4,2)=2, matchRatio=2/2=1.0 → 20*(0.5+0.5*1.0)=20
        // 분위기: 1/1=1.0 → 15*(0.5+0.5*1.0)=15
        // 스타일: 1/1=1.0 → 10*(0.5+0.5*1.0)=10
        // 기본 30 + 25 + 20 + 15 + 10 = 100
        assertThat(score).isEqualTo(100);
    }

    @Test
    @DisplayName("큐레이션 태그가 많고 사용자 매칭이 적을 때 점진적 감소")
    void calculate_lowMatchRatio_lowerScore() {
        // given
        Curation curation = Curation.builder()
                .genres(List.of("소설", "에세이", "역사", "철학"))
                .keywords(List.of("위로", "성장", "공감", "사랑"))
                .moods(List.of("퇴근 후", "카페", "비 오는 날", "서점에서"))
                .styles(List.of("몰입형", "정독형", "속독형", "비평적"))
                .build();

        ReadingPreferenceInfo preference = new ReadingPreferenceInfo(
                1L,
                "INFP",
                Set.of(),
                Set.of(),
                List.of(),
                List.of("퇴근 후", "카페", "비 오는 날", "서점에서"),
                List.of("소설", "에세이", "역사", "철학"),
                List.of("위로", "성장", "공감", "사랑"),
                List.of("몰입형", "정독형", "속독형", "비평적")
        );

        // 1개만 매칭되는 케이스
        ReadingPreferenceInfo lowMatchPreference = new ReadingPreferenceInfo(
                2L,
                "INFP",
                Set.of(),
                Set.of(),
                List.of(),
                List.of("퇴근 후", "새벽 시간", "늦은 밤", "혼자만의 시간"),
                List.of("소설", "과학", "경제", "심리학"),
                List.of("위로", "유머", "추리", "모험"),
                List.of("몰입형", "창의적", "실용적", "가볍게 즐기기")
        );

        // when
        int fullMatchScore = SimilarityMatcher.calculate(curation, preference);
        int lowMatchScore = SimilarityMatcher.calculate(curation, lowMatchPreference);

        // then
        // 전체 매칭: 각 카테고리 4/4=1.0 → 최대 점수
        assertThat(fullMatchScore).isEqualTo(100);

        // 1/4 매칭: 각 카테고리 matchRatio=1/4=0.25
        // 장르: 25*(0.5+0.5*0.25)=25*0.625=16 (반올림)
        // 키워드: 20*(0.5+0.5*0.25)=20*0.625=13 (반올림)
        // 분위기: 15*(0.5+0.5*0.25)=15*0.625=9 (반올림)
        // 스타일: 10*(0.5+0.5*0.25)=10*0.625=6 (반올림)
        // 기본 30 + 16 + 13 + 9 + 6 = 74
        assertThat(lowMatchScore).isEqualTo(74);

        // 핵심 검증: 매칭 비율이 높을수록 점수가 높다
        assertThat(fullMatchScore).isGreaterThan(lowMatchScore);
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
