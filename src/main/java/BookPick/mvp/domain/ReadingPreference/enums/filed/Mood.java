package BookPick.mvp.domain.ReadingPreference.enums.filed;

public enum Mood implements PreferenceField {
    AFTER_WORK("퇴근 후"),
    HOT_TEA("따뜻한 차 한잔"),
    RAINY_DAY("비 오는 날"),
    SNOWY_DAY("눈 오는 날"),
    SUBWAY_BUS("지하철·버스"),
    CAFE("카페"),
    BED("침대에서"),
    PARK("공원"),
    LIBRARY("도서관"),
    BOOKSTORE("서점에서"),
    DAWN("새벽 시간"),
    WEEKEND_AFTERNOON("주말 오후"),
    LUNCH_TIME("점심시간"),
    LATE_NIGHT("늦은 밤"),
    BEFORE_SLEEP("잠들기 전"),
    ALONE_TIME("혼자만의 시간"),
    WINDOW_SIDE("창가에서"),
    WITH_MUSIC("음악과 함께"),
    TRAVELING("여행 중"),
    VACATION("휴가 중");

    private final String description;

    Mood(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static boolean isValid(String description) {
        return PreferenceField.isValidDescription(Mood.class, description);
    }
}
