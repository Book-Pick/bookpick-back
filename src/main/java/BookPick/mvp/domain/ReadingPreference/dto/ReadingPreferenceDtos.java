package BookPick.mvp.domain.ReadingPreference.dto;


import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;
import BookPick.mvp.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ReadingPreferenceDtos {


    // -- register --
    public record ReadingPreferenceRegisterReq(
            String mbti,
//            @NotNull List<String> favoriteAuthors,      // 좋아하는 작가
            List<String> favoriteBooks,        // 좋아하는 책
            List<String> moods,    // 독서 선호 분위기
            List<String> readingHabits,        // 독서 습관
            List<String> genres,      // 선호 장르
            List<String> keywords,              // 키워드
            List<String> trends              //
    ) {
    }

    public record ReadingPreferenceRegisterRes(
            Long readingPreferenceId
    ) {
        static public ReadingPreferenceRegisterRes from(ReadingPreference readingPreference) {
            return new ReadingPreferenceRegisterRes(readingPreference.getId());
        }
    }

}




