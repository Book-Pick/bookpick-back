package BookPick.mvp.domain.curation.util.list.similarity;

import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.entity.Curation;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 큐레이션-사용자 유사도 계산기
 *
 * 카테고리별 가중치와 점진적 매칭 비율을 반영한 유사도 계산
 *
 * 점수 구성:
 * - 기본: 30점 (1개 이상 매칭 시에만 부여)
 * - 장르 매칭: 최대 25점 (첫 매칭 50% 보장 + 매칭 비율에 비례)
 * - 키워드 매칭: 최대 20점 (첫 매칭 50% 보장 + 매칭 비율에 비례)
 * - 분위기 매칭: 최대 15점 (첫 매칭 50% 보장 + 매칭 비율에 비례)
 * - 스타일 매칭: 최대 10점 (첫 매칭 50% 보장 + 매칭 비율에 비례)
 * - 작가 매칭: 10점
 * = 최대 110점 → 100점으로 cap
 */
public class SimilarityMatcher {

    // 기본 점수
    private static final int BASE_SCORE = 30;
    private static final int MAX_SCORE = 100;

    // 카테고리별 최대 가중치 (합계: 70)
    private static final int GENRE_MAX_WEIGHT = 25;
    private static final int KEYWORD_MAX_WEIGHT = 20;
    private static final int MOOD_MAX_WEIGHT = 15;
    private static final int STYLE_MAX_WEIGHT = 10;

    // 작가 매칭 보너스
    private static final int AUTHOR_BONUS = 10;

    // 첫 매칭 기본 비율 및 비례 증가 비율
    private static final double FIRST_MATCH_RATIO = 0.5;
    private static final double ADDITIONAL_MATCH_RATIO = 0.5;

    private SimilarityMatcher() {}

    /**
     * 유사도 계산
     */
    public static int calculate(Curation curation, ReadingPreferenceInfo preferenceInfo) {
        int score = 0;

        // 1. 장르 매칭 (최대 25점)
        score += calculateCategoryScore(
                curation.getGenres(),
                preferenceInfo.genres(),
                GENRE_MAX_WEIGHT);

        // 2. 키워드 매칭 (최대 20점)
        score += calculateCategoryScore(
                curation.getKeywords(),
                preferenceInfo.keywords(),
                KEYWORD_MAX_WEIGHT);

        // 3. 분위기 매칭 (최대 15점)
        score += calculateCategoryScore(
                curation.getMoods(),
                preferenceInfo.moods(),
                MOOD_MAX_WEIGHT);

        // 4. 스타일 매칭 (최대 10점)
        score += calculateCategoryScore(
                curation.getStyles(),
                preferenceInfo.readingStyles(),
                STYLE_MAX_WEIGHT);

        // 5. 작가 매칭 보너스 (최대 10점)
        score += calculateAuthorBonus(
                curation.getBookAuthor(),
                preferenceInfo.favoriteAuthors());

        // 6. 1개라도 매칭되면 기본 점수 부여
        if (score > 0) {
            score += BASE_SCORE;
        }

        return Math.min(score, MAX_SCORE);
    }

    /**
     * 카테고리별 점수 계산
     *
     * 첫 매칭 시 최대 가중치의 50%를 부여하고,
     * 나머지 50%는 매칭 비율(matchCount / possibleMatches)에 비례하여 부여한다.
     *
     * 예시 (maxWeight = 20, possibleMatches = 4):
     *   0개 매칭 →  0점
     *   1개 매칭 → 12점 (50% + 50%×1/4 = 62.5%)
     *   2개 매칭 → 15점 (50% + 50%×2/4 = 75%)
     *   3개 매칭 → 17점 (50% + 50%×3/4 = 87.5%)
     *   4개 매칭 → 20점 (50% + 50%×4/4 = 100%)
     */
    private static int calculateCategoryScore(
            List<String> curationItems,
            List<String> userPreferences,
            int maxWeight) {

        if (curationItems == null || userPreferences == null) {
            return 0;
        }

        int matchCount = (int) curationItems.stream()
                .filter(userPreferences::contains)
                .count();

        if (matchCount == 0) {
            return 0;
        }

        int possibleMatches = Math.min(curationItems.size(), userPreferences.size());
        double matchRatio = (double) matchCount / possibleMatches;

        // 첫 매칭 50% 보장 + 나머지 50%는 매칭 비율에 비례
        double score = maxWeight * (FIRST_MATCH_RATIO + ADDITIONAL_MATCH_RATIO * matchRatio);

        return (int) Math.round(score);
    }

    /**
     * 작가 매칭 보너스 계산
     */
    private static int calculateAuthorBonus(
            String bookAuthor,
            Set<Author> favoriteAuthors) {

        if (bookAuthor == null || favoriteAuthors == null || favoriteAuthors.isEmpty()) {
            return 0;
        }

        boolean authorMatched = favoriteAuthors.stream()
                .anyMatch(author -> bookAuthor.equals(author.getName()));

        return authorMatched ? AUTHOR_BONUS : 0;
    }

    /**
     * 매칭된 항목들의 요약 문자열 생성
     */
    public static String getMatchedSummary(Curation curation, ReadingPreferenceInfo preferenceInfo) {
        StringBuilder sb = new StringBuilder();

        appendMatched(sb, curation.getGenres(), preferenceInfo.genres());
        appendMatched(sb, curation.getKeywords(), preferenceInfo.keywords());
        appendMatched(sb, curation.getMoods(), preferenceInfo.moods());
        appendMatched(sb, curation.getStyles(), preferenceInfo.readingStyles());

        return sb.toString();
    }

    private static void appendMatched(StringBuilder sb, List<String> curationItems, List<String> userPreferences) {
        if (curationItems == null || userPreferences == null) {
            return;
        }
        List<String> matched = curationItems.stream()
                .filter(userPreferences::contains)
                .toList();

        if (!matched.isEmpty()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(String.join(", ", matched));
        }
    }
}
