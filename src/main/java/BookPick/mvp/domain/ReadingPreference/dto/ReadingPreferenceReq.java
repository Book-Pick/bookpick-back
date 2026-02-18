package BookPick.mvp.domain.ReadingPreference.dto;

import BookPick.mvp.domain.author.dto.preference.AuthorDto;
import BookPick.mvp.domain.book.dto.preference.BookDto;
import java.util.List;
import java.util.Set;

public record ReadingPreferenceReq(
        String mbti,
        Set<BookDto> favoriteBooks, // 좋아하는 책
        Set<AuthorDto> favoriteAuthors,
        List<String> moods, // 독서 선호 분위기
        List<String> readingHabits, // 독서 습관
        List<String> genres, // 선호 장르
        List<String> keywords, // 키워드
        List<String> readingStyles //
        ) {}
