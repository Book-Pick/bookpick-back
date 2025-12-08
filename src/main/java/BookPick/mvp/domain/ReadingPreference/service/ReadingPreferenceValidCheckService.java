package BookPick.mvp.domain.ReadingPreference.service;

import BookPick.mvp.domain.ReadingPreference.Exception.WrongReadingPreferenceRequestException;
import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceReq;
import BookPick.mvp.domain.ReadingPreference.enums.filed.*;
import org.springframework.stereotype.Service;

@Service
public class ReadingPreferenceValidCheckService {

    public void validate(ReadingPreferenceReq req) {

        // 🔥 MBTI 검증 (null 허용)
        if (req.mbti() != null && !req.mbti().isEmpty()) {
            if (!MBTI.isValid(req.mbti())) {
                throw new WrongReadingPreferenceRequestException();
            }
        }

        // 🔥 moods 검증
        if (req.moods() != null) {
            for (String mood : req.moods()) {
                if (!Mood.isValid(mood)) {
                    throw new WrongReadingPreferenceRequestException();
                }
            }
        }

        // 🔥 readingHabits 검증
        if (req.readingHabits() != null) {
            for (String habit : req.readingHabits()) {
                if (!ReadingHabit.isValid(habit)) {
                    throw new WrongReadingPreferenceRequestException();
                }
            }
        }

        // 🔥 genres 검증
        if (req.genres() != null) {
            for (String genre : req.genres()) {
                if (!Genre.isValid(genre)) {
                    throw new WrongReadingPreferenceRequestException();
                }
            }
        }

        // 🔥 keywords 검증
        if (req.keywords() != null) {
            for (String keyword : req.keywords()) {
                if (!Keyword.isValid(keyword)) {
                    throw new WrongReadingPreferenceRequestException();
                }
            }
        }

        // 🔥 readingStyles 검증
        if (req.readingStyles() != null) {
            for (String style : req.readingStyles()) {
                if (!ReadingStyle.isValid(style)) {
                    throw new WrongReadingPreferenceRequestException();
                }
            }
        }
    }
}
