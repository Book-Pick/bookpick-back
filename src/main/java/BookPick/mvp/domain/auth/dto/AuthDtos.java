package BookPick.mvp.domain.auth.dto;


import BookPick.mvp.domain.auth.service.MyUserDetailsService.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public class AuthDtos {

    // -- SignUp --
    public record SignReq(
            @NotBlank @Email String email,
            @Size(min = 8, max = 72) String password
    ) {
    }

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
    ) {
    }

    public record LoginRes(
            long userId,
            String email,
            String nickname,
            String bio,
            String profileImageUrl,
            boolean isFirstLogin,

            String accessToken
    ) {

        public static LoginRes from(CustomUserDetails customUserDetails, String accessToken) {
            return new LoginRes(
                    customUserDetails.getId(),
                    customUserDetails.getUsername(),       // username = email
                    customUserDetails.getNickname(),
                    customUserDetails.getBio(),
                    customUserDetails.getProfileImageUrl(),
                    customUserDetails.isFirstLogin(),
                    accessToken
            );
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
}

