package BookPick.mvp.domain.ReadingPreference.enums.filed;

public enum ReadingHabit implements PreferenceField{

    READ_AT_ONCE("한 번에 완독하는 편"),
    HIGHLIGHTING("밑줄 긋거나 형광펜으로 표시하는 편"),
    MULTIPLE_BOOKS("여러 권을 동시에 읽는 편"),
    MANY_BOOKMARKS("책갈피를 많이 사용하는 편"),
    NOTE_TAKING("읽은 내용을 메모하는 편"),
    READ_OUT_LOUD("소리 내어 읽는 편"),
    QUIET_PLACE("조용한 곳에서만 읽는 편"),
    WITH_MUSIC("음악을 들으며 읽는 편"),
    REREADING("읽은 책을 다시 읽는 편"),
    READING_CLUB("독서 모임에 참여하는 편");

    private final String description;

    ReadingHabit(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static boolean isValid(String description) {
        return PreferenceField.isValidDescription(ReadingHabit.class, description);
    }
}
