package BookPick.mvp.domain.preference.dto.Create;


import BookPick.mvp.domain.preference.entity.ReadingPreference;

public record ReadingPreferenceCreateRes(
            Long readingPreferenceId
    ) {
        static public ReadingPreferenceCreateRes from(ReadingPreference readingPreference) {
            return new ReadingPreferenceCreateRes(readingPreference.getId());
        }
    }





