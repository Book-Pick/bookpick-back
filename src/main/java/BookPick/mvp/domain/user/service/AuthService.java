package BookPick.mvp.domain.user.service;

import BookPick.mvp.domain.user.dto.AuthDtos.*;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.DuplicateEmailException;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        user.setRole("normal_user");   // normal_user, curator

        // 3. DB 저장
        User savedUser = userRepository.save(user);

        // 4. 응답
        return new SignRes(savedUser.getId());
    }



    public AuthRes login(LoginReq req){

        // 1. 이메일 검증
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 이메일 혹은 비밀번호입니다.");
        }

        // 3. JWT 토큰 발급 (❌ 비밀번호 넣지 말고)
        String accessToken = jwtUtil.createToken(user);

        // refreshToken 발급도 필요하다면 JwtUtil에서 따로 만들기 -> MVP에선 미구현

        // 4. AuthRes 응답
        return new AuthRes(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getBio(),
                user.getProfileImageUrl(),
                accessToken
        );
    }
}
