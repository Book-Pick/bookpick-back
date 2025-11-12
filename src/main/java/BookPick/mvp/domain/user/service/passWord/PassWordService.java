package BookPick.mvp.domain.user.service.passWord;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.kakao_boot_camp.domain.user.dto.passWord.PassWordChangeReq;
import springboot.kakao_boot_camp.domain.user.exception.PasswordMismatchException;
import springboot.kakao_boot_camp.domain.user.exception.UserNotFoundException;
import springboot.kakao_boot_camp.domain.user.exception.WrongCurrentPasswordException;
import springboot.kakao_boot_camp.domain.user.model.User;
import springboot.kakao_boot_camp.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PassWordService {
    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;


    // 1. 비밀번호 변경
    // TODO 1. 자동입력 방지 문자 추가
    @Transactional
    public void PassWordChange(Long userId, PassWordChangeReq req) {
        if (userId == null) {
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 1) 새로운 비밀번호 컨펌 비밀번호 체크
        if (!req.newPassWord().equals(req.confirmPassword())) {
            throw new PasswordMismatchException();
        }

        // 2) 입력받은 비밀번호와 기존 비밀번호 체크
        if (!passwordEncoder.matches(req.nowPassWord(), user.getPassWord())) {
            throw new WrongCurrentPasswordException();
        }

        // 3) 기존 비밀번호를 입력받은 비밀번호로 변경
        user.setPassWord(passwordEncoder.encode(req.newPassWord()));


    }


}
