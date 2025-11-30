package BookPick.mvp.domain.user.service.passWord;

import BookPick.mvp.domain.user.dto.passWord.PassWordChangeReq;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.PasswordMismatchException;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.exception.common.WrongCurrentPasswordException;
import BookPick.mvp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (!passwordEncoder.matches(req.nowPassWord(), user.getPassword())) {
            throw new WrongCurrentPasswordException();
        }

        // 3) 기존 비밀번호를 입력받은 비밀번호로 변경
        user.setPassword(passwordEncoder.encode(req.newPassWord()));


    }


}
