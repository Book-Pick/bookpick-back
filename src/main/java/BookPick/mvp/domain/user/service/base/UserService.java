package BookPick.mvp.domain.user.service.base;

import BookPick.mvp.domain.user.dto.base.UserReq;
import BookPick.mvp.domain.user.dto.base.UserRes;
import BookPick.mvp.domain.user.dto.base.delete.UserSoftDeleteRes;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.AlreadyDeletedException;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.exception.profile.UserNameNotNullException;
import BookPick.mvp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    final private UserRepository userRepository;

    // 1. 유저 생성            (관리자 권한)
    public UserRes CreateUser(UserReq req) {

        User user = User.builder()
                .email(req.email())
                .password(req.passWord())
                .nickname(req.nickName())
                .profileImageUrl(req.profileImage())
                .role(req.role())
                .build();

        User result = userRepository.save(user);


        return UserRes.from(result);
    }



    // 2.1 개인 정보 조회  (개인 권한)
    public UserRes userProfileGet(Long userId) {

        User result = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return UserRes.from(result);
    }



    // 3.1 유저 수정
    @Transactional
    public UserRes userProfileUpdate(Long userId, UserReq req) {

        if (userId == null) {
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 더티 체킹
        if (req.email() != null) user.setEmail(req.email());
        if (req.nickName() == null) {
            throw new UserNameNotNullException();
        }
            user.setNickname(req.nickName());
        if (req.profileImage() != null) user.setProfileImageUrl(req.profileImage());
        if (req.introduction() != null) user.setBio(req.introduction());

        return UserRes.from(user);

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
