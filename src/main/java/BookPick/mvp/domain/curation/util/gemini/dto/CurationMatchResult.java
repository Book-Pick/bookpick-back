package BookPick.mvp.domain.curation.util.gemini.dto;

import BookPick.mvp.domain.curation.model.Curation;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CurationMatchResult {
    private Curation curation;
    private String matchedMood;
    private String matchedGenre;
    private String matchedKeyword;
    private String matchedStyle;
    private int totalMatchCount;
    private String matched;

    public static CurationMatchResult of(Curation curation,
                                         String recommendedMood,
                                         String recommendedGenre,
                                         String recommendedKeyword,
                                         String recommendedStyle) {

        String matchedMood = null;
        String matchedGenre = null;
        String matchedKeyword = null;
        String matchedStyle = null;
        int matchCount = 0;
        List<String> matchedItems = new ArrayList<>();

        // Mood 매칭
        if (curation.getMoods() != null && curation.getMoods().contains(recommendedMood)) {
            matchedMood = recommendedMood;
            matchedItems.add(recommendedMood);
            matchCount++;
        }

        // Genre 매칭
        if (curation.getGenres() != null && curation.getGenres().contains(recommendedGenre)) {
            matchedGenre = recommendedGenre;
            matchedItems.add(recommendedGenre);
            matchCount++;
        }

        // Keyword 매칭
        if (curation.getKeywords() != null && curation.getKeywords().contains(recommendedKeyword)) {
            matchedKeyword = recommendedKeyword;
            matchedItems.add(recommendedKeyword);
            matchCount++;
        }

        // Style 매칭
        if (curation.getStyles() != null && curation.getStyles().contains(recommendedStyle)) {
            matchedStyle = recommendedStyle;
            matchedItems.add(recommendedStyle);
            matchCount++;
        }

        String matchedString = String.join(", ", matchedItems);

        return CurationMatchResult.builder()
                .curation(curation)
                .matchedMood(matchedMood)
                .matchedGenre(matchedGenre)
                .matchedKeyword(matchedKeyword)
                .matchedStyle(matchedStyle)
                .totalMatchCount(matchCount)
                .matched(matchedString)
                .build();
    }

    // 일치한 것만 문자열로 반환
    public String getMatchedString() {
        StringBuilder sb = new StringBuilder();

        if (matchedMood != null) {
            sb.append("Mood: ").append(matchedMood).append("\n");
        }
        if (matchedGenre != null) {
            sb.append("Genre: ").append(matchedGenre).append("\n");
        }
        if (matchedKeyword != null) {
            sb.append("Keyword: ").append(matchedKeyword).append("\n");
        }
        if (matchedStyle != null) {
            sb.append("Style: ").append(matchedStyle).append("\n");
        }

        return sb.toString();
    }
}
