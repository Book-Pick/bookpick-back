package BookPick.mvp.domain.ReadingPreference.service;

import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceReq;
import BookPick.mvp.domain.ReadingPreference.enums.filed.*;
import org.springframework.stereotype.Service;

@Service
public class ReadingPreferenceValidCheck {

    public boolean checkReadingPreferenceReqIsValid(ReadingPreferenceReq req) {

        // 🔥 MBTI 검증 (null 허용)
        if (req.mbti() != null && !req.mbti().isEmpty()) {
            if (!MBTI.isValid(req.mbti())) {
                return false;
            }
        }

        // 🔥 moods 검증
        if (req.moods() != null) {
            for (String mood : req.moods()) {
                if (!Mood.isValid(mood)) {
                    return false;
                }
            }
        }

        // 🔥 readingHabits 검증
        if (req.readingHabits() != null) {
            for (String habit : req.readingHabits()) {
                if (!ReadingHabit.isValid(habit)) {
                    return false;
                }
            }
        }

        // 🔥 genres 검증
        if (req.genres() != null) {
            for (String genre : req.genres()) {
                if (!Genre.isValid(genre)) {
                    return false;
                }
            }
        }

        // 🔥 keywords 검증
        if (req.keywords() != null) {
            for (String keyword : req.keywords()) {
                if (!Keyword.isValid(keyword)) {
                    return false;
                }
            }
        }

        // 🔥 readingStyles 검증
        if (req.readingStyles() != null) {
            for (String style : req.readingStyles()) {
                if (!ReadingStyle.isValid(style)) {
                    return false;
                }
            }
        }

        return true;
    }
}
