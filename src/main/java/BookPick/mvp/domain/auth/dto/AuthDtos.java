package BookPick.mvp.domain.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {


    // -- SignUp --
    public record SignReq(
            @NotBlank @Email String email,
            @Size(min = 8, max = 72) String password
    ) {}
    public record SignRes(
            long userId
    ) {
        public static SignRes from(long userId) {
            return new SignRes(userId);
        }
    }


    // -- Login --
    public record LoginReq(
            @NotBlank @Email String email,
            @Size(min = 8, max = 72) String password
    ) {}
    public record AuthRes(
            long userId,
            String email,
            String nickname,
            String bio,
            String profileImageUrl,
            String accessToken
    ) {
    }


    // 3. 로그아
    public record LogoutReq(
            @NotBlank String refreshToken
    ) {
    }

    // 3. 토큰 재발급 요청
    public record RefreshToken(
            @NotBlank String refreshToken
    ) {
    }
}
