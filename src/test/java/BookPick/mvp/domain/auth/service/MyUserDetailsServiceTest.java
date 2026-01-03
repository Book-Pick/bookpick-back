package BookPick.mvp.domain.auth.service;

import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetailsService 테스트")
class MyUserDetailsServiceTest {

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("정상 사용자 조회 - 일반 유저")
    void loadUserByUsername_success_normalUser() {
        // given
        String email = "test@test.com";
        User mockUser = User.builder()
                .id(1L)
                .email(email)
                .password("encodedPassword")
                .nickname("테스터")
                .bio("자기소개")
                .profileImageUrl("profile.jpg")
                .role(Roles.ROLE_USER)
                .isFirstLogin(true)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // when
        UserDetails result = myUserDetailsService.loadUserByUsername(email);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(CustomUserDetails.class);
        assertThat(result.getUsername()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo("encodedPassword");

        CustomUserDetails customResult = (CustomUserDetails) result;
        assertThat(customResult.getId()).isEqualTo(1L);
        assertThat(customResult.getNickname()).isEqualTo("테스터");
        assertThat(customResult.getBio()).isEqualTo("자기소개");
        assertThat(customResult.getProfileImageUrl()).isEqualTo("profile.jpg");
        assertThat(customResult.isFirstLogin()).isTrue();

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("권한 부여 확인 - ROLE_USER")
    void loadUserByUsername_roleUser_granted() {
        // given
        String email = "user@test.com";
        User mockUser = User.builder()
                .id(1L)
                .email(email)
                .password("password")
                .role(Roles.ROLE_USER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // when
        UserDetails result = myUserDetailsService.loadUserByUsername(email);

        // then
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 - 예외 발생")
    void loadUserByUsername_fail_userNotFound() {
        // given
        String email = "notexist@test.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> myUserDetailsService.loadUserByUsername(email))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("CustomUserDetails 필드 전체 검증")
    void loadUserByUsername_allFieldsSet() {
        // given
        String email = "complete@test.com";
        User mockUser = User.builder()
                .id(999L)
                .email(email)
                .password("hashedPassword123")
                .nickname("완전한사용자")
                .bio("완전한 자기소개입니다")
                .profileImageUrl("https://example.com/profile.jpg")
                .role(Roles.ROLE_USER)
                .isFirstLogin(false)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // when
        UserDetails result = myUserDetailsService.loadUserByUsername(email);

        // then
        CustomUserDetails customResult = (CustomUserDetails) result;
        assertThat(customResult.getId()).isEqualTo(999L);
        assertThat(customResult.getUsername()).isEqualTo(email);
        assertThat(customResult.getPassword()).isEqualTo("hashedPassword123");
        assertThat(customResult.getNickname()).isEqualTo("완전한사용자");
        assertThat(customResult.getBio()).isEqualTo("완전한 자기소개입니다");
        assertThat(customResult.getProfileImageUrl()).isEqualTo("https://example.com/profile.jpg");
        assertThat(customResult.isFirstLogin()).isFalse();
        assertThat(customResult.getAuthorities()).hasSize(1);

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("닉네임이 null인 경우")
    void loadUserByUsername_nullNickname_success() {
        // given
        String email = "nonickname@test.com";
        User mockUser = User.builder()
                .id(1L)
                .email(email)
                .password("password")
                .nickname(null)
                .role(Roles.ROLE_USER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // when
        UserDetails result = myUserDetailsService.loadUserByUsername(email);

        // then
        CustomUserDetails customResult = (CustomUserDetails) result;
        assertThat(customResult.getNickname()).isNull();

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("여러 번 호출해도 정상 동작")
    void loadUserByUsername_multipleCalls_success() {
        // given
        String email = "multi@test.com";
        User mockUser = User.builder()
                .id(1L)
                .email(email)
                .password("password")
                .role(Roles.ROLE_USER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // when
        UserDetails result1 = myUserDetailsService.loadUserByUsername(email);
        UserDetails result2 = myUserDetailsService.loadUserByUsername(email);

        // then
        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result1.getUsername()).isEqualTo(result2.getUsername());

        verify(userRepository, times(2)).findByEmail(email);
    }

    @Test
    @DisplayName("프로필 이미지 URL이 없는 경우")
    void loadUserByUsername_noProfileImage_success() {
        // given
        String email = "noimage@test.com";
        User mockUser = User.builder()
                .id(1L)
                .email(email)
                .password("password")
                .nickname("이미지없음")
                .profileImageUrl(null)
                .role(Roles.ROLE_USER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // when
        UserDetails result = myUserDetailsService.loadUserByUsername(email);

        // then
        CustomUserDetails customResult = (CustomUserDetails) result;
        assertThat(customResult.getProfileImageUrl()).isNull();
        assertThat(customResult.getNickname()).isEqualTo("이미지없음");

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("첫 로그인 플래그 확인")
    void loadUserByUsername_firstLoginFlag() {
        // given
        String email1 = "first@test.com";
        String email2 = "second@test.com";

        User firstLoginUser = User.builder()
                .id(1L)
                .email(email1)
                .password("password")
                .role(Roles.ROLE_USER)
                .isFirstLogin(true)
                .build();

        User returningUser = User.builder()
                .id(2L)
                .email(email2)
                .password("password")
                .role(Roles.ROLE_USER)
                .isFirstLogin(false)
                .build();

        when(userRepository.findByEmail(email1)).thenReturn(Optional.of(firstLoginUser));
        when(userRepository.findByEmail(email2)).thenReturn(Optional.of(returningUser));

        // when
        CustomUserDetails firstResult = (CustomUserDetails) myUserDetailsService.loadUserByUsername(email1);
        CustomUserDetails secondResult = (CustomUserDetails) myUserDetailsService.loadUserByUsername(email2);

        // then
        assertThat(firstResult.isFirstLogin()).isTrue();
        assertThat(secondResult.isFirstLogin()).isFalse();
    }
}
