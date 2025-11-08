package BookPick.mvp.domain.preference.dto.Update;

import java.util.List;

public record ReadingPreferenceUpdateReq(
        Long preferenceId,
        String mbti,
        List<String> favoriteAuthors,        // 좋아하는 책
        List<String> favoriteBooks,        // 좋아하는 책
        List<String> moods,    // 독서 선호 분위기
        List<String> readingHabits,        // 독서 습관
        List<String> genres,      // 선호 장르
        List<String> keywords,              // 키워드
        List<String> trends   
) {
}
