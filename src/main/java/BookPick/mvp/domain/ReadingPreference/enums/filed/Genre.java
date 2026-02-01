package BookPick.mvp.domain.ReadingPreference.enums.filed;

public enum Genre implements PreferenceField {
    NOVEL("소설"),
    ESSAY("에세이"),
    HISTORY("역사"),
    ART("예술"),
    SELF_DEVELOPMENT("자기개발"),
    ECONOMY("경제"),
    PSYCHOLOGY("심리학"),
    SOCIETY("사회"),
    EDUCATION("교육"),
    SCIENCE("과학"),
    PHILOSOPHY("철학"),
    RELIGION("종교");

    private final String description;

    Genre(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    // ✔ description 기반 유효성 검증 (한 줄 래핑)
    public static boolean isValid(String description) {
        return PreferenceField.isValidDescription(Genre.class, description);
    }
}
