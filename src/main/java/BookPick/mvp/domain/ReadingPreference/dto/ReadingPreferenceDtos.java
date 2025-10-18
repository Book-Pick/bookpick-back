package BookPick.mvp.domain.ReadingPreference.dto;


import BookPick.mvp.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ReadingPreferenceDtos {


    // -- register --
    public record ReadingPreferenceRegisterReq(
            @NotBlank String mbti,
//            @NotNull List<String> favoriteAuthors,      // 좋아하는 작가
            @NotNull List<String> favoriteBooks,        // 좋아하는 책
            @NotNull List<String> mood,    // 독서 선호 분위기
            @NotNull List<String> readingHabits,        // 독서 습관
            @NotNull List<String> preferredGenres,      // 선호 장르
            @NotNull List<String> keywords,              // 키워드
            @NotNull List<String> recommendedTrends              //
    ) {
    }
    public record ReadingPreferenceRegisterRes(
            Long readingPreferenceId
    ){
        public ReadingPreferenceRegisterRes from(User user){
            return new ReadingPreferenceRegisterRes(user.getId());
        }
    }

    }




