package BookPick.mvp.domain.user.dto;

import jakarta.validation.constraints.*;


public class UserDtos {

    public record Res(
            Long id,
            String nickName,
            String email
    ){}

    // 사용처 : 회원 가입,
    public record CreateReq(
        @Email @NotBlank String email,
        @Size(min=8, max = 72 ) @NotBlank String password
        ){}
}
