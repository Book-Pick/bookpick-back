package BookPick.mvp.domain.ReadingPreference.service;

import BookPick.mvp.domain.ReadingPreference.Exception.WrongReadingPreferenceRequestException;
import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceReq;
import BookPick.mvp.domain.ReadingPreference.enums.filed.*;
import org.springframework.stereotype.Service;

@Service
public class ReadingPreferenceValidCheckService {

    public void validateReadingPreferenceReq(ReadingPreferenceReq req) {

        if (req.mbti() != null && !req.mbti().isEmpty()) {
            if (!MBTI.isValid(req.mbti())) {
                throw new WrongReadingPreferenceRequestException();
            }
        }

        if (req.moods() != null) {
            for (String mood : req.moods()) {
                if (!Mood.isValid(mood)) {
                    throw new WrongReadingPreferenceRequestException();
                }
            }
        }

        if (req.readingHabits() != null) {
            for (String habit : req.readingHabits()) {
                if (!ReadingHabit.isValid(habit)) {
                    throw new WrongReadingPreferenceRequestException();
                }
            }
        }

        if (req.genres() != null) {
            for (String genre : req.genres()) {
                if (!Genre.isValid(genre)) {
                    throw new WrongReadingPreferenceRequestException();
                }
            }
        }

        if (req.keywords() != null) {
            for (String keyword : req.keywords()) {
                if (!Keyword.isValid(keyword)) {
                    throw new WrongReadingPreferenceRequestException();
                }
            }
        }

        if (req.readingStyles() != null) {
            for (String style : req.readingStyles()) {
                if (!ReadingStyle.isValid(style)) {
                    throw new WrongReadingPreferenceRequestException();
                }
            }
        }
    }
}
