package BookPick.mvp.domain.ReadingPreference.enums.filed;

public enum ReadingStyle implements PreferenceField {

    SPEED_READING("속독형"),
    IMMERSIVE("몰입형"),
    CAREFUL_READING("정독형"),
    EXPLORATORY("취향 탐색형"),
    STORY_FOCUSED("스토리 중심"),
    KNOWLEDGE_BASED("지식 위주"),
    EMOTIONAL("감성적"),
    LOGICAL("논리적"),
    CREATIVE("창의적"),
    PRACTICAL("실용적"),
    CRITICAL("비평적"),
    IMAGINATIVE("상상력 중시"),
    RELAXED("느긋한 독서"),
    DEEP_THINKING("깊이 있는 사색"),
    LIGHT_READING("가볍게 즐기기");

    private final String description;

    ReadingStyle(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static boolean isValid(String description) {
        return PreferenceField.isValidDescription(ReadingStyle.class, description);
    }
}
