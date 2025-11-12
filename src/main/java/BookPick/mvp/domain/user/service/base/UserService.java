package BookPick.mvp.domain.user.service.base;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.kakao_boot_camp.domain.user.dto.base.create.UserCreateReq;
import springboot.kakao_boot_camp.domain.user.dto.base.create.UserCreateRes;
import springboot.kakao_boot_camp.domain.user.dto.base.read.UserGetRes;
import springboot.kakao_boot_camp.domain.user.dto.base.soft.UserSoftDeleteRes;
import springboot.kakao_boot_camp.domain.user.dto.base.update.UserUpdateReq;
import springboot.kakao_boot_camp.domain.user.dto.base.update.UserUpdateRes;
import springboot.kakao_boot_camp.domain.user.exception.AlreadyDeletedException;
import springboot.kakao_boot_camp.domain.user.exception.UserNotFoundException;
import springboot.kakao_boot_camp.domain.user.model.User;
import springboot.kakao_boot_camp.domain.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    final private UserRepository userRepository;

    // 1. 유저 생성            (관리자 권한)
    public UserCreateRes CreateUser(UserCreateReq req) {

        User user = User.builder()
                .email(req.email())
                .passWord(req.passWord())
                .nickName(req.nickName())
                .profileImage(req.profileImage())
                .role(req.role())
                .build();

        User result = userRepository.save(user);


        return UserCreateRes.from(result);
    }



    // 2.1 개인 정보 조회  (개인 권한)
    public UserGetRes userProfileGet(Long userId) {

        User result = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return UserGetRes.from(result);
    }



    // 3.1 유저 수정
    @Transactional
    public UserUpdateRes userProfileUpdate(Long userId, UserUpdateReq req) {

        if (userId == null) {
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 더티 체킹
        if (req.email() != null) user.setEmail(req.email());
        if (req.nickName() != null) user.setNickName(req.nickName());
        if (req.profileImage() != null) user.setProfileImage(req.profileImage());

        return UserUpdateRes.from(user);

    }



    // 4.1 유저 소프트 삭제
    @Transactional
    public UserSoftDeleteRes softDeleteProfile(Long userId) {

        // 델리트 1로 바꾸고 삭제 시간 업데이트
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (user.isDeleted()) {
            throw new AlreadyDeletedException();
        }

        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());

        return UserSoftDeleteRes.from(user);
    }
    // 4.2 유저 하드 삭제
    @Transactional
    public void hardDeleteUserProfile(Long userId) {

        // 델리트 1로 바꾸고 삭제 시간 업데이트
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        userRepository.deleteById(userId);

    }


}
