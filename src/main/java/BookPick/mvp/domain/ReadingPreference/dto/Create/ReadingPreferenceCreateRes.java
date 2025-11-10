package BookPick.mvp.domain.ReadingPreference.dto.Create;


import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;

public record ReadingPreferenceCreateRes(
            Long readingPreferenceId
    ) {
        static public ReadingPreferenceCreateRes from(ReadingPreference readingPreference) {
            return new ReadingPreferenceCreateRes(readingPreference.getId());
        }
    }





