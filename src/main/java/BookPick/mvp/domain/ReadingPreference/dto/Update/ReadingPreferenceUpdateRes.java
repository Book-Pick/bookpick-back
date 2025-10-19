package BookPick.mvp.domain.ReadingPreference.dto.Update;

import BookPick.mvp.domain.ReadingPreference.dto.Get.ReadingPreferenceGetRes;
import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;

import java.util.List;

public record ReadingPreferenceUpdateRes(
        Long preferenceId,
        String mbti,
        List<String> favoriteBooks,        // 좋아하는 책
        List<String> moods,    // 독서 선호 분위기
        List<String> readingHabits,        // 독서 습관
        List<String> genres,      // 선호 장르
        List<String> keywords,              // 키워드
        List<String> trends
) {
    static public ReadingPreferenceUpdateRes from(ReadingPreference rp){
        return new ReadingPreferenceUpdateRes(
                rp.getId(),
                rp.getMbti(),
                rp.getFavoriteBooks(),
                rp.getMoods(),
                rp.getReadingHabits(),
                rp.getGenres(),
                rp.getKeywords(),
                rp.getTrends()
        );
    }
}
