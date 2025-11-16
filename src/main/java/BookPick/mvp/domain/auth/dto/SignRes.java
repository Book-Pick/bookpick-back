package BookPick.mvp.domain.auth.dto;

public record SignRes(
        long userId
) {
    public static SignRes from(long userId) {
        return new SignRes(userId);
    }
}
