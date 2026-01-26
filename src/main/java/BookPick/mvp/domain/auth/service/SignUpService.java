package BookPick.mvp.domain.auth.service;


import BookPick.mvp.domain.ReadingPreference.service.ReadingPreferenceService;
import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.auth.dto.SignReq;
import BookPick.mvp.domain.auth.dto.SignRes;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import BookPick.mvp.domain.auth.exception.DuplicateEmailException;
import BookPick.mvp.domain.auth.util.Manager.signup.SignUpManager;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class SignUpService {      //Dto로 컨트롤러에서 받음

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final SignUpManager signUpManager;

    private final ReadingPreferenceService readingPreferenceService;

    public SignRes signUp(SignReq req) throws RuntimeException {


        // 1. 중복 확인
        if (userRepo.existsByEmail(req.email())) {
            throw new DuplicateEmailException();
        }


        Roles userRole = Roles.ROLE_USER;

        if (signUpManager.isAdmin( req.email())){
            userRole=Roles.ROLE_ADMIN;
        }

        User user = User.builder()
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .nickname(null)
                .profileImageUrl(null)
                .role(userRole)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        // 3. DB에 저장
        User savedUSer = userRepo.save(user);

        // 4. 빈 독서취향 생성
        readingPreferenceService.addClearReadingPreference(savedUSer.getId());

        // 5. Sign Response DTO 반환
        return new SignRes(savedUSer.getId());

    }



}

