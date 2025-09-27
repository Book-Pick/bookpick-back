package BookPick.mvp.domain.auth.service;

import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.auth.dto.AuthDtos.*;
import BookPick.mvp.domain.auth.exception.InvalidLoginException;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.auth.exception.DuplicateEmailException;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.global.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @Transactional
    public SignRes signUp(SignReq req) {
        // 1. 이메일 중복 확인
        if (userRepository.existsByEmail(req.email())) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
        }

        // 2. 신규 유저 생성
        User user = new User();
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole(Roles.ROLE_USER);   // normal_user, curator

        // 3. DB 저장
        User savedUser = userRepository.save(user);

        // 4. 응답
        return new SignRes(savedUser.getId());
    }




    // access Token 만 전송, refresh x
    @Transactional(readOnly = true)
    public AuthRes login(LoginReq req, HttpServletResponse res) {
        var authToken = new UsernamePasswordAuthenticationToken(req.email(), req.password());
        try {
            var auth = authenticationManagerBuilder.getObject().authenticate(authToken);

            // Access 토큰만 발급
            String access = JwtUtil.createAccessToken(auth);

            var principal = (MyUserDetailsService.CustomUser) auth.getPrincipal();

            // 선택: 응답 헤더에도 추가해주면 프론트가 꺼내 쓰기 쉬움
            res.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + access);

            return new AuthRes(
                    principal.getId(),
                    principal.getUsername(),   // email
                    principal.getNickname(),
                    principal.getBio(),
                    principal.getProfileImageUrl(),
                    access                     // 프론트가 Authorization: Bearer 로 전송
            );
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new InvalidLoginException("아이디 또는 비밀번호가 잘못되었습니다.");
        } catch (AuthenticationException e) {
            throw new InvalidLoginException("로그인 실패");
        }
    }

}
