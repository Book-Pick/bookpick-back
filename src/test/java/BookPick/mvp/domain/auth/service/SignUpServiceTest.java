package BookPick.mvp.domain.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import BookPick.mvp.domain.ReadingPreference.service.ReadingPreferenceService;
import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.auth.dto.SignReq;
import BookPick.mvp.domain.auth.dto.SignRes;
import BookPick.mvp.domain.auth.exception.DuplicateEmailException;
import BookPick.mvp.domain.auth.util.Manager.signup.SignUpManager;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원가입 서비스 테스트")
class SignUpServiceTest {

    @InjectMocks private SignUpService signUpService;

    @Mock private UserRepository userRepository;

    @Mock private PasswordEncoder passwordEncoder;

    @Mock private SignUpManager signUpManager;

    @Mock private ReadingPreferenceService readingPreferenceService;

    @Test
    @DisplayName("정상 회원가입 - 일반 유저")
    void signUp_success_normalUser() {
        // given
        SignReq req = new SignReq("test@test.com", "password123");
        User mockUser =
                User.builder()
                        .id(1L)
                        .email(req.email())
                        .password("encodedPassword")
                        .role(Roles.ROLE_USER)
                        .build();

        when(userRepository.existsByEmail(req.email())).thenReturn(false);
        when(signUpManager.isAdmin(req.email())).thenReturn(false);
        when(passwordEncoder.encode(req.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // when
        SignRes result = signUpService.signUp(req);

        // then
        assertThat(result.userId()).isEqualTo(1L);
        verify(userRepository).existsByEmail(req.email());
        verify(passwordEncoder).encode(req.password());
        verify(userRepository).save(any(User.class));
        verify(readingPreferenceService).addClearReadingPreference(1L);
    }

    @Test
    @DisplayName("정상 회원가입 - 관리자")
    void signUp_success_adminUser() {
        // given
        SignReq req = new SignReq("admin@bookpick.com", "password123");
        User mockUser =
                User.builder()
                        .id(1L)
                        .email(req.email())
                        .password("encodedPassword")
                        .role(Roles.ROLE_ADMIN)
                        .build();

        when(userRepository.existsByEmail(req.email())).thenReturn(false);
        when(signUpManager.isAdmin(req.email())).thenReturn(true);
        when(passwordEncoder.encode(req.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // when
        SignRes result = signUpService.signUp(req);

        // then
        assertThat(result.userId()).isEqualTo(1L);
        verify(signUpManager).isAdmin(req.email());
        verify(userRepository).save(argThat(user -> user.getRole() == Roles.ROLE_ADMIN));
    }

    @Test
    @DisplayName("이메일 중복 - 예외 발생")
    void signUp_fail_duplicateEmail() {
        // given
        SignReq req = new SignReq("duplicate@test.com", "password123");
        when(userRepository.existsByEmail(req.email())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> signUpService.signUp(req))
                .isInstanceOf(DuplicateEmailException.class);

        verify(userRepository).existsByEmail(req.email());
        verify(userRepository, never()).save(any(User.class));
        verify(readingPreferenceService, never()).addClearReadingPreference(anyLong());
    }

    @Test
    @DisplayName("비밀번호 암호화 확인")
    void signUp_passwordEncoded() {
        // given
        SignReq req = new SignReq("test@test.com", "plainPassword");
        String encodedPassword = "encrypted_password_hash";

        User mockUser = User.builder().id(1L).email(req.email()).password(encodedPassword).build();

        when(userRepository.existsByEmail(req.email())).thenReturn(false);
        when(signUpManager.isAdmin(req.email())).thenReturn(false);
        when(passwordEncoder.encode(req.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // when
        signUpService.signUp(req);

        // then
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository)
                .save(
                        argThat(
                                user ->
                                        user.getPassword().equals(encodedPassword)
                                                && !user.getPassword().equals("plainPassword")));
    }
}
