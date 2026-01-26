package BookPick.mvp.domain.auth.service;

import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.auth.dto.LoginReq;
import BookPick.mvp.domain.auth.dto.LoginRes;
import BookPick.mvp.domain.auth.exception.InvalidLoginException;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.JwtAuthManager;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("로그인 서비스 테스트")
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private JwtAuthManager jwtAuthManager;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("정상 로그인 - 첫 로그인")
    void login_success_firstLogin() {
        // given
        LoginReq req = new LoginReq("test@test.com", "password123");
        User mockUser = User.builder()
                .id(1L)
                .email(req.email())
                .password("encodedPassword")
                .nickname("테스터")
                .role(Roles.ROLE_USER)
                .isFirstLogin(true)
                .build();

        CustomUserDetails mockUserDetails = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        mockUserDetails.setId(mockUser.getId());
        mockUserDetails.setNickname(mockUser.getNickname());
        mockUserDetails.setFirstLogin(mockUser.isFirstLogin());

        JwtAuthManager.TokenPair tokenPair = new JwtAuthManager.TokenPair(
                "access_token",
                "refresh_token"
        );

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);
        when(jwtAuthManager.createTokens(authentication)).thenReturn(tokenPair);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // when
        LoginRes result = loginService.login(req, httpServletRequest);

        // then
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.accessToken()).isEqualTo("access_token");
        assertThat(result.refreshToken()).isEqualTo("refresh_token");
        assertThat(result.isFirstLogin()).isTrue();

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtAuthManager).createTokens(authentication);
        verify(userRepository).findById(1L);
        assertThat(mockUser.isFirstLogin()).isFalse(); // 첫 로그인 플래그 업데이트 확인
    }

    @Test
    @DisplayName("정상 로그인 - 재로그인")
    void login_success_notFirstLogin() {
        // given
        LoginReq req = new LoginReq("test@test.com", "password123");
        User mockUser = User.builder()
                .id(1L)
                .email(req.email())
                .password("encodedPassword")
                .nickname("테스터")
                .role(Roles.ROLE_USER)
                .isFirstLogin(false)
                .build();

        CustomUserDetails mockUserDetails = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        mockUserDetails.setId(mockUser.getId());
        mockUserDetails.setNickname(mockUser.getNickname());
        mockUserDetails.setFirstLogin(mockUser.isFirstLogin());

        JwtAuthManager.TokenPair tokenPair = new JwtAuthManager.TokenPair(
                "access_token",
                "refresh_token"
        );

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);
        when(jwtAuthManager.createTokens(authentication)).thenReturn(tokenPair);

        // when
        LoginRes result = loginService.login(req, httpServletRequest);

        // then
        assertThat(result.isFirstLogin()).isFalse();
        verify(userRepository, never()).findById(anyLong()); // 첫 로그인이 아니면 업데이트 안함
    }

    @Test
    @DisplayName("잘못된 비밀번호 - 예외 발생")
    void login_fail_wrongPassword() {
        // given
        LoginReq req = new LoginReq("test@test.com", "wrongPassword");

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // when & then
        assertThatThrownBy(() -> loginService.login(req, httpServletRequest))
                .isInstanceOf(InvalidLoginException.class);

        verify(jwtAuthManager, never()).createTokens(any());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 - 예외 발생")
    void login_fail_userNotFound() {
        // given
        LoginReq req = new LoginReq("notexist@test.com", "password123");

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("User not found"));

        // when & then
        assertThatThrownBy(() -> loginService.login(req, httpServletRequest))
                .isInstanceOf(InvalidLoginException.class);

        verify(jwtAuthManager, never()).createTokens(any());
    }

    @Test
    @DisplayName("JWT 토큰 생성 확인")
    void login_tokenGeneration() {
        // given
        LoginReq req = new LoginReq("test@test.com", "password123");
        User mockUser = User.builder()
                .id(1L)
                .email(req.email())
                .password("encodedPassword")
                .role(Roles.ROLE_USER)
                .isFirstLogin(false)
                .build();

        CustomUserDetails mockUserDetails = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        mockUserDetails.setId(mockUser.getId());
        mockUserDetails.setNickname(mockUser.getNickname());
        mockUserDetails.setFirstLogin(mockUser.isFirstLogin());

        JwtAuthManager.TokenPair tokenPair = new JwtAuthManager.TokenPair(
                "generated_access_token",
                "generated_refresh_token"
        );

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);
        when(jwtAuthManager.createTokens(authentication)).thenReturn(tokenPair);

        // when
        LoginRes result = loginService.login(req, httpServletRequest);

        // then
        assertThat(result.accessToken()).isNotNull();
        assertThat(result.refreshToken()).isNotNull();
        assertThat(result.accessToken()).isEqualTo("generated_access_token");
        assertThat(result.refreshToken()).isEqualTo("generated_refresh_token");

        verify(jwtAuthManager).createTokens(authentication);
    }
}
