package BookPick.mvp.domain.user.service;

import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.domain.user.dto.UserDtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_INSTANT;
    private final PasswordEncoder passwordEncoder;


    // 회원가입, DB에 추가
    public Res create(CreateReq req) {
        if (repo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("email-duplicated");
        }
        User u = new User();
        u.setEmail(req.email());
        u.setPassword(passwordEncoder.encode((req.password()))); // 실제로는 BCrypt 권장
        repo.save(u);
        return new Res(u.getId(),u.getNickname(), u.getEmail());

    }
}
