package BookPick.mvp.domain.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {



    // 1. 회원가입
    //Req
     public record SignReq(
            @NotBlank @Email String email,
            @Size(min=8, max = 72 ) String password
    ){ }

    //res
    public record SignRes(
            long userId
    ){
    }


    // 2. 로그인
    //Req
    public record LoginReq(
            @NotBlank @Email String email,
            @Size(min=8, max = 72 ) String password
    ){}

    //Res
    public record AuthRes(
        long userId,
        String email,
        String nickname,
        String bio,
        String profileImageUrl,
        String accessToken,
        String refreshToken,   // 👈 추가
        long   expiresIn       // 👈 선택: Access 만료(초)

) {}




    // 3. 로그아
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
