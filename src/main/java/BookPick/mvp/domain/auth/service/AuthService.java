package BookPick.mvp.domain.auth.service;

import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.auth.dto.Create.AuthDtos.*;
import BookPick.mvp.domain.auth.exception.DuplicateEmailException;
import BookPick.mvp.domain.auth.exception.InvalidLoginException;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

        // 1. 중복 확인
        if (userRepository.existsAllByEmail((req.email()))) {
            throw new DuplicateEmailException();
        }

        // 2. 신규 유저 생성
        User user = User.builder()
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .role(Roles.ROLE_USER)
                .build();

        // 3. DB 저장
        User saved = userRepository.save(user);

        // 4. 응답
        return SignRes.from(saved.getId());
    }


    // access Token O, refresh X
    @Transactional
    public LoginRes login(LoginReq req, HttpServletResponse res) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(req.email(), req.password());   // 임시로 이메일과 아이디가 담김

        try {
            // getObject : AuthenticationManager 객체 반환
            // .authenticate : Authenticate를 상속한 구현체의 인스턴스를 검증한다.
            Authentication auth = authenticationManagerBuilder.getObject().authenticate(authToken);        // -> UserDetailsService.loadUserByUsername(), 비밀 번호 및 아이디 검증

            firstLoginCheck(req.email());

            String accessToken = JwtUtil.createAccessToken(auth);    // Access O
            String refreshToken = JwtUtil.createRefreshToken(auth);  // Refresh X


            MyUserDetailsService.CustomUserDetails customUserDetails = (MyUserDetailsService.CustomUserDetails) auth.getPrincipal();

            return LoginRes.from(customUserDetails, "Bearer " + accessToken);

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new InvalidLoginException();
        } catch (AuthenticationException e) {
            throw new InvalidLoginException();
        }
    }

    @Transactional
    void firstLoginCheck(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (user.isFirstLogin()) {
            user.isNotFirstLogin();     // 더티체킹
        }
    }

}
