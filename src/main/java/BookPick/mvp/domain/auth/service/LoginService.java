package BookPick.mvp.domain.auth.service;

import BookPick.mvp.domain.auth.dto.LoginReq;
import BookPick.mvp.domain.auth.dto.LoginRes;
import BookPick.mvp.domain.auth.exception.InvalidLoginException;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.JwtAuthManager;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final JwtAuthManager jwtAuthManager;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;

    // 1. jwt 기반 로그인
    @Transactional
    public LoginRes login(LoginReq req, HttpServletRequest servletReq) throws RuntimeException {

        // 1. 토큰 생성
        var token = new UsernamePasswordAuthenticationToken(req.email(), req.password());

        // 2. 로그인 검증
        try {

            // 아이디 및 패스워드 확인
            var auth = authenticationManagerBuilder.getObject().authenticate(token);

            // Jwt 토큰 생성
            JwtAuthManager.TokenPair tokenPair = jwtAuthManager.createTokens(auth);

            //
            LoginRes res =
                    LoginRes.from(
                            (CustomUserDetails) auth.getPrincipal(),
                            tokenPair.accessToken(),
                            tokenPair.refreshToken());

            if (res.isFirstLogin()) {
                User user =
                        userRepository
                                .findById(res.userId())
                                .orElseThrow(UserNotFoundException::new);

                user.setFirstLogin(false);
            }
            return res;

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new InvalidLoginException();
        } catch (AuthenticationException e) {
            throw new InvalidLoginException();
        }
    }
}
