package BookPick.mvp.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// -- Login --
public record LoginReq(@NotBlank @Email String email, @Size(min = 8, max = 72) String password) {}
