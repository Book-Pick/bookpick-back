package BookPick.mvp.domain.auth;

public enum Roles {
    ROLE_USER("일반 사용자"),
    ROLE_CURATOR("큐레이터"),
    ROLE_OWNER("서점 주인"),
    ROLE_ADMIN("운영자");

    private final String description;

    Roles(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
