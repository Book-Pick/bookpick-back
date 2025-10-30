package BookPick.mvp.domain.preference.dto.Create;

import java.util.List;

public record ReadingPreferenceCreateReq(
        String mbti,
        List<String> favoriteBooks,        // 좋아하는 책
        List<String> moods,    // 독서 선호 분위기
        List<String> readingHabits,        // 독서 습관
        List<String> genres,      // 선호 장르
        List<String> keywords,              // 키워드
        List<String> trends              //
) {
}
