package BookPick.mvp.domain.ReadingPreference.enums.filed;

public enum Keyword implements PreferenceField {

    COMFORT("위로"),
    GROWTH("성장"),
    LOVE("사랑"),
    EMPATHY("공감"),
    KNOWLEDGE("지식"),
    HUMOR("유머"),
    MYSTERY("추리"),
    ADVENTURE("모험"),
    FANTASY("판타지"),
    REALITY("현실"),
    FUTURE("미래"),
    PAST("과거"),
    HORROR("공포");

    private final String description;

    Keyword(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static boolean isValid(String description) {
        return PreferenceField.isValidDescription(Keyword.class, description);
    }
}

