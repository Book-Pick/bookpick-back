package BookPick.mvp.domain.auth.service;

import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.auth.exception.JwtTokenExpiredException;
import BookPick.mvp.domain.auth.exception.NotAuthenticateUser;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.TokenBlacklistManager;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("로그아웃 서비스 테스트")
class LogoutServiceTest {

    @InjectMocks
    private LogoutService logoutService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private TokenBlacklistManager tokenBlacklistManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    @DisplayName("정상 로그아웃 성공")
    void logout_success() {
        // given
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .role(Roles.ROLE_USER)
                .build();

        CustomUserDetails currentUser = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        currentUser.setId(1L);

        String refreshToken = "valid_refresh_token";
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);

        Claims claims = mock(Claims.class);
        when(claims.get("userId", Number.class)).thenReturn(1);
        when(claims.getId()).thenReturn("jti-12345");
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 3600000)); // 1시간 후

        when(request.getCookies()).thenReturn(new Cookie[]{refreshCookie});
        when(jwtUtil.extractRefreshToken(refreshToken)).thenReturn(claims);

        // when
        logoutService.logout(currentUser, request, response);

        // then
        verify(jwtUtil).extractRefreshToken(refreshToken);
        verify(tokenBlacklistManager).add(eq("jti-12345"), any(Instant.class));
        verify(response).addCookie(argThat(cookie ->
                cookie.getName().equals("refreshToken") &&
                cookie.getValue() == null &&
                cookie.getMaxAge() == 0 &&
                cookie.isHttpOnly() &&
                cookie.getSecure()
        ));
    }

    @Test
    @DisplayName("미인증 사용자 로그아웃 시도 - 예외 발생")
    void logout_fail_notAuthenticated() {
        // given
        CustomUserDetails currentUser = null;

        // when & then
        assertThatThrownBy(() -> logoutService.logout(currentUser, request, response))
                .isInstanceOf(NotAuthenticateUser.class);

        verify(tokenBlacklistManager, never()).add(anyString(), any(Instant.class));
        verify(response, never()).addCookie(any(Cookie.class));
    }

    @Test
    @DisplayName("쿠키가 없는 경우 - 정상 종료")
    void logout_noCookies_success() {
        // given
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .build();

        CustomUserDetails currentUser = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        currentUser.setId(1L);

        when(request.getCookies()).thenReturn(null);

        // when
        logoutService.logout(currentUser, request, response);

        // then
        verify(jwtUtil, never()).extractRefreshToken(anyString());
        verify(tokenBlacklistManager, never()).add(anyString(), any(Instant.class));
        verify(response, never()).addCookie(any(Cookie.class));
    }

    @Test
    @DisplayName("리프레시 토큰이 빈 값인 경우 - 정상 종료")
    void logout_emptyRefreshToken_success() {
        // given
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .build();

        CustomUserDetails currentUser = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        currentUser.setId(1L);

        Cookie emptyCookie = new Cookie("refreshToken", "");

        when(request.getCookies()).thenReturn(new Cookie[]{emptyCookie});

        // when
        logoutService.logout(currentUser, request, response);

        // then
        verify(jwtUtil, never()).extractRefreshToken(anyString());
        verify(tokenBlacklistManager, never()).add(anyString(), any(Instant.class));
        verify(response, never()).addCookie(any(Cookie.class));
    }

    @Test
    @DisplayName("만료된 토큰으로 로그아웃 시도 - 예외 발생")
    void logout_fail_expiredToken() {
        // given
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .build();

        CustomUserDetails currentUser = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        currentUser.setId(1L);

        String expiredToken = "expired_refresh_token";
        Cookie refreshCookie = new Cookie("refreshToken", expiredToken);

        when(request.getCookies()).thenReturn(new Cookie[]{refreshCookie});
        when(jwtUtil.extractRefreshToken(expiredToken))
                .thenThrow(new RuntimeException("Token expired"));

        // when & then
        assertThatThrownBy(() -> logoutService.logout(currentUser, request, response))
                .isInstanceOf(JwtTokenExpiredException.class);

        verify(tokenBlacklistManager, never()).add(anyString(), any(Instant.class));
    }

    @Test
    @DisplayName("다른 쿠키들 사이에 리프레시 토큰이 있는 경우")
    void logout_withMultipleCookies_success() {
        // given
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .build();

        CustomUserDetails currentUser = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        currentUser.setId(1L);

        String refreshToken = "valid_refresh_token";
        Cookie[] cookies = new Cookie[]{
                new Cookie("sessionId", "session123"),
                new Cookie("refreshToken", refreshToken),
                new Cookie("otherCookie", "value")
        };

        Claims claims = mock(Claims.class);
        when(claims.get("userId", Number.class)).thenReturn(1);
        when(claims.getId()).thenReturn("jti-12345");
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 3600000));

        when(request.getCookies()).thenReturn(cookies);
        when(jwtUtil.extractRefreshToken(refreshToken)).thenReturn(claims);

        // when
        logoutService.logout(currentUser, request, response);

        // then
        verify(jwtUtil).extractRefreshToken(refreshToken);
        verify(tokenBlacklistManager).add(eq("jti-12345"), any(Instant.class));
        verify(response).addCookie(any(Cookie.class));
    }

    @Test
    @DisplayName("토큰 블랙리스트 추가 확인 - TTL 설정")
    void logout_blacklistTTL_correct() {
        // given
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .build();

        CustomUserDetails currentUser = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        currentUser.setId(1L);

        String refreshToken = "valid_refresh_token";
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);

        long expirationTime = System.currentTimeMillis() + 7200000; // 2시간 후
        Claims claims = mock(Claims.class);
        when(claims.get("userId", Number.class)).thenReturn(1);
        when(claims.getId()).thenReturn("jti-unique");
        when(claims.getExpiration()).thenReturn(new Date(expirationTime));

        when(request.getCookies()).thenReturn(new Cookie[]{refreshCookie});
        when(jwtUtil.extractRefreshToken(refreshToken)).thenReturn(claims);

        // when
        logoutService.logout(currentUser, request, response);

        // then
        verify(tokenBlacklistManager).add(
                eq("jti-unique"),
                argThat(instant -> {
                    long expectedMillis = expirationTime - System.currentTimeMillis();
                    long actualMillis = instant.toEpochMilli() - System.currentTimeMillis();
                    // 1초 정도 오차 허용
                    return Math.abs(actualMillis - expectedMillis) < 1000;
                })
        );
    }

    @Test
    @DisplayName("만료된 토큰 (음수 TTL) - 블랙리스트 추가하지 않음")
    void logout_expiredToken_notAddedToBlacklist() {
        // given
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .build();

        CustomUserDetails currentUser = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        currentUser.setId(1L);

        String refreshToken = "almost_expired_token";
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);

        Claims claims = mock(Claims.class);
        when(claims.get("userId", Number.class)).thenReturn(1);
        when(claims.getId()).thenReturn("jti-expired");
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() - 1000)); // 이미 만료됨

        when(request.getCookies()).thenReturn(new Cookie[]{refreshCookie});
        when(jwtUtil.extractRefreshToken(refreshToken)).thenReturn(claims);

        // when
        logoutService.logout(currentUser, request, response);

        // then
        verify(tokenBlacklistManager, never()).add(anyString(), any(Instant.class));
        verify(response).addCookie(any(Cookie.class)); // 쿠키는 여전히 삭제됨
    }
}
