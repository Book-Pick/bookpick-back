package BookPick.mvp.domain.preference.dto;


import BookPick.mvp.domain.preference.entity.UserPreference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PreferenceDtos {


    // 1. 취향 설정

    // Req
    public record CreateReq(
            @NotBlank String mbti,
            @NotNull List<String> favoriteAuthors,      // 좋아하는 작가
            @NotNull List<String> favoriteBooks,      // 좋아하는 작가
            @NotNull List<String> selectionCriteria,    // 독서 선호 분위기
            @NotNull List<String> readingHabits,        // 독서 습관
            @NotNull List<String> preferredGenres,      // 선호 장르
            @NotNull List<String> keywords,              // 키워드
            @NotNull List<String> recommendedTrends              //
    ){}



    // 2. 취향 수정
    public record UpdateReq(
            @NotBlank String mbti,
            @NotNull List<String> favoriteAuthors,
            @NotNull List<String> selectionCriteria,
            @NotNull List<String> readingHabits,
            @NotNull List<String> preferredGenres,
            @NotNull List<String> keywords,
            @NotNull List<String> recommendedTrends
    ) {}


    // PreferenceRes (MVP용 단순화)
    public record PreferenceRes(
            Long id,
            String mbti,
            List<String> favoriteAuthors,
            List<String> favoriteBooks,
            List<String> selectionCriteria,
            List<String> readingHabits,
            List<String> preferredGenres,
            List<String> keywords,
            List<String> recommendedTrends
    ) {
        public static PreferenceRes from(UserPreference p) {
    return new PreferenceRes(
        p.getId(),
        p.getMbti(),
        p.getFavoriteAuthors(),
        p.getFavoriteBooks(),
        p.getSelectionCriteria(),
        p.getReadingHabits(),
        p.getPreferredGenres(),
        p.getKeywords(),
        p.getRecommendedTrends()
    );
}

    }



}




