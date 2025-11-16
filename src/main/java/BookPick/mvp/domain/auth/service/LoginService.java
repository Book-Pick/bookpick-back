package BookPick.mvp.domain.auth.service;

import BookPick.mvp.domain.auth.dto.LoginReq;
import BookPick.mvp.domain.auth.dto.LoginRes;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import BookPick.mvp.domain.auth.exception.InvalidLoginException;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.JwtAuthManager;


@Service
@RequiredArgsConstructor
public class LoginService {
    private final JwtAuthManager jwtAuthManager;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    // 1. jwt 기반 로그인
    public LoginRes login(LoginReq req, HttpServletRequest servletReq) throws RuntimeException {

        // 1. 토큰 생성
        var token = new UsernamePasswordAuthenticationToken(req.email(), req.password());

        // 2. 로그인 검증
        try {
            var auth = authenticationManagerBuilder.getObject().authenticate(token);


            JwtAuthManager.TokenPair tokenPair = jwtAuthManager.createTokens(auth);


            return LoginRes.from((CustomUserDetails) auth.getPrincipal(), tokenPair.accessToken(), tokenPair.refreshToken());

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new InvalidLoginException();
        } catch (AuthenticationException e) {
            throw new InvalidLoginException();
        }
    }

}

