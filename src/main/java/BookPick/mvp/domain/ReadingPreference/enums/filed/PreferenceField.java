package BookPick.mvp.domain.ReadingPreference.enums.filed;

import java.util.function.Function;


import java.util.function.Function;

public interface PreferenceField {

    String getDescription();

    static <E extends Enum<E> & PreferenceField> boolean isValidDescription(
            Class<E> enumClass,
            String description
    ) {
        if (description == null || description.isEmpty()) {
            return true;
        }
        for (E constant : enumClass.getEnumConstants()) {
            if (constant.getDescription().equals(description)) {
                return true;
            }
        }
        return false;
    }
}

