//package BookPick.mvp.domain.ReadingPreference.dto.ETC.Update;
//
//import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;
//
//import java.util.List;
//
//public record ReadingPreferenceRes(
//        Long preferenceId,
//        String mbti,
//        List<String> favoriteBooks,        // 좋아하는 책
//        List<String> moods,    // 독서 선호 분위기
//        List<String> readingHabits,        // 독서 습관
//        List<String> genres,      // 선호 장르
//        List<String> keywords,              // 키워드
//        List<String> trends
//) {
//    static public ReadingPreferenceRes from(ReadingPreference rp){
//        return new ReadingPreferenceRes(
//                rp.getId(),
//                rp.getMbti(),
//                rp.getFavoriteBooks(),
//                rp.getMoods(),
//                rp.getReadingHabits(),
//                rp.getGenres(),
//                rp.getKeywords(),
//                rp.getTrends()
//        );
//    }
//}
