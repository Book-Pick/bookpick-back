package BookPick.mvp.domain.ReadingPreference.dto;


import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;
import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.book.entity.Book;

import java.util.List;
import java.util.Set;

public record ReadingPreferenceRes(
        Long preferenceId,
        String mbti,
        Set<Book> favoriteBooks,        // 좋아하는 책
        Set<Author> favoriteAuthors,
        List<String> moods,    // 독서 선호 분위기
        List<String> readingHabits,        // 독서 습관
        List<String> genres,      // 선호 장르
        List<String> keywords,              // 키워드
        List<String> readingStyles              //
) {

    static public ReadingPreferenceRes from(ReadingPreference rp) {
        return new ReadingPreferenceRes(
                rp.getId(),
                rp.getMbti(),
                rp.getFavoriteBooks(),
                rp.getFavoriteAuthors(),
                rp.getMoods(),
                rp.getReadingHabits(),
                rp.getGenres(),
                rp.getKeywords(),
                rp.getReadingStyles()
        );

    }
}





