package BookPick.mvp.domain.user.enums.user;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_USER("user"),
    ROLE_ADMIN("admin");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }
    ;
}
