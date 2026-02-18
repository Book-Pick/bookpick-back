package BookPick.mvp.domain.user.dto.passWord;

import jakarta.validation.constraints.NotNull;

public record PassWordChangeReq(
        @NotNull String nowPassWord, @NotNull String newPassWord, @NotNull String confirmPassword) {

    public static PassWordChangeReq from(
            String nowPassWord, String newPassWord, String confirmPassword) {
        return new PassWordChangeReq(nowPassWord, newPassWord, confirmPassword);
    }
}
