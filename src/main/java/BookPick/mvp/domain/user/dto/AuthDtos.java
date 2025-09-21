package BookPick.mvp.domain.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {



    // 1. 회원가입
    public record SignReq(
            @NotBlank @Email String email,
            @Size(min=8, max = 72 ) String password
    ){ }

    public record SignRes(
            long user_id
    ){
    }


    // 2. 로그인
    public record LoginReq(
            @NotBlank @Email String email,
            @Size(min=8, max = 72 ) String password
    ){}

    //로그인시
    public record AuthRes(
        long user_id,
        String email,
        String nickname,
        String bio,
        String profile_image_url,
        String access_token
) {}




    // 3. 로그아웃
    public record LogoutReq(
        @NotBlank String refreshToken
    ){
    }

    // 3. 토큰 재발급 요청
    public record RefreshToken(
        @NotBlank String refreshToken
    ){
    }
}
