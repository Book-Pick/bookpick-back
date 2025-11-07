package BookPick.mvp.domain.curation.dto.prefer;

import BookPick.mvp.domain.preference.entity.ReadingPreference;

import java.util.List;

public record ReadingPreferenceInfo(
        Long userId,
        String mbti,
        List<String> favoriteBooks,
        List<String> moods,
        List<String> readingHabits,
        List<String> genres,
        List<String> keywords,
        List<String> trends
) {

    // 엔티티 → DTO 변환
    public static ReadingPreferenceInfo from(ReadingPreference preference) {
        return new ReadingPreferenceInfo(
                preference.getUser().getId(),
                preference.getMbti(),
                preference.getFavoriteBooks(),
                preference.getMoods(),
                preference.getReadingHabits(),
                preference.getGenres(),
                preference.getKeywords(),
                preference.getTrends()
        );
    }
}
