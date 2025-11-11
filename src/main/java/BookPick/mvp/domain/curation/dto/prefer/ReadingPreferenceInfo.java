package BookPick.mvp.domain.curation.dto.prefer;

import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;
import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.book.entity.Book;

import java.util.List;

public record ReadingPreferenceInfo(
        Long userId,
        String mbti,
        List<Book> favoriteBooks,
        List<Author> favoriteAuthors,
        List<String> readingHabits,
        List<String> moods,
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
                preference.getFavoriteAuthors(),
                preference.getMoods(),
                preference.getReadingHabits(),
                preference.getGenres(),
                preference.getKeywords(),
                preference.getTrends()
        );
    }

}
