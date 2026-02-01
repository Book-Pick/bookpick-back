package BookPick.mvp.domain.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.auth.dto.LoginRes;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.JwtAuthManager;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.TokenBlacklistManager;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@ExtendWith(MockitoExtension.class)
@DisplayName("토큰 갱신 서비스 테스트")
class TokenRefreshServiceTest {

    @InjectMocks private TokenRefreshService tokenRefreshService;

    @Mock private JwtAuthManager jwtAuthManager;

    @Mock private TokenBlacklistManager tokenBlacklistManager;

    @Mock private JwtUtil jwtUtil;

    @Test
    @DisplayName("정상 토큰 갱신 성공")
    void refreshTokens_success() {
        // given
        User mockUser =
                User.builder()
                        .id(1L)
                        .email("test@test.com")
                        .password("password")
                        .nickname("테스터")
                        .role(Roles.ROLE_USER)
                        .build();

        CustomUserDetails customUserDetails =
                new CustomUserDetails(
                        mockUser,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        customUserDetails.setId(1L);
        customUserDetails.setNickname("테스터");

        String refreshToken = "valid_refresh_token";

        Claims claims = mock(Claims.class);
        when(claims.get("userId", Double.class)).thenReturn(1.0); // Double로 저장

        JwtAuthManager.TokenPair newTokenPair =
                new JwtAuthManager.TokenPair("new_access_token", "new_refresh_token");

        when(tokenBlacklistManager.isBlacklisted(refreshToken)).thenReturn(false);
        when(jwtUtil.validateToken(refreshToken, false)).thenReturn(true);
        when(jwtUtil.extractRefreshToken(refreshToken)).thenReturn(claims);
        when(jwtAuthManager.createTokens(any())).thenReturn(newTokenPair);

        // when
        LoginRes result = tokenRefreshService.refreshTokens(customUserDetails, refreshToken);

        // then
        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.accessToken()).isEqualTo("new_access_token");
        assertThat(result.refreshToken()).isEqualTo("new_refresh_token");

        verify(tokenBlacklistManager).isBlacklisted(refreshToken);
        verify(jwtUtil).validateToken(refreshToken, false);
        verify(jwtUtil).extractRefreshToken(refreshToken);
        verify(jwtAuthManager).createTokens(any());
    }

    @Test
    @DisplayName("토큰이 null인 경우 - 예외 발생")
    void refreshTokens_fail_nullToken() {
        // given
        User mockUser = User.builder().id(1L).email("test@test.com").password("password").build();

        CustomUserDetails customUserDetails =
                new CustomUserDetails(
                        mockUser,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        customUserDetails.setId(1L);

        // when & then
        assertThatThrownBy(() -> tokenRefreshService.refreshTokens(customUserDetails, null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("리프레시 토큰이 없습니다");

        verify(tokenBlacklistManager, never()).isBlacklisted(anyString());
        verify(jwtAuthManager, never()).createTokens(any());
    }

    @Test
    @DisplayName("토큰이 빈 문자열인 경우 - 예외 발생")
    void refreshTokens_fail_blankToken() {
        // given
        User mockUser = User.builder().id(1L).email("test@test.com").password("password").build();

        CustomUserDetails customUserDetails =
                new CustomUserDetails(
                        mockUser,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        customUserDetails.setId(1L);

        // when & then
        assertThatThrownBy(() -> tokenRefreshService.refreshTokens(customUserDetails, "   "))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("리프레시 토큰이 없습니다");

        verify(tokenBlacklistManager, never()).isBlacklisted(anyString());
    }

    @Test
    @DisplayName("블랙리스트에 등록된 토큰 - 예외 발생")
    void refreshTokens_fail_blacklistedToken() {
        // given
        User mockUser = User.builder().id(1L).email("test@test.com").password("password").build();

        CustomUserDetails customUserDetails =
                new CustomUserDetails(
                        mockUser,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        customUserDetails.setId(1L);

        String blacklistedToken = "blacklisted_token";

        when(tokenBlacklistManager.isBlacklisted(blacklistedToken)).thenReturn(true);

        // when & then
        assertThatThrownBy(
                        () ->
                                tokenRefreshService.refreshTokens(
                                        customUserDetails, blacklistedToken))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("유효하지 않은 리프레시 토큰입니다");

        verify(tokenBlacklistManager).isBlacklisted(blacklistedToken);
        verify(jwtUtil, never()).validateToken(anyString(), anyBoolean());
        verify(jwtAuthManager, never()).createTokens(any());
    }

    @Test
    @DisplayName("만료된 토큰 - 예외 발생")
    void refreshTokens_fail_expiredToken() {
        // given
        User mockUser = User.builder().id(1L).email("test@test.com").password("password").build();

        CustomUserDetails customUserDetails =
                new CustomUserDetails(
                        mockUser,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        customUserDetails.setId(1L);

        String expiredToken = "expired_token";

        when(tokenBlacklistManager.isBlacklisted(expiredToken)).thenReturn(false);
        when(jwtUtil.validateToken(expiredToken, false)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> tokenRefreshService.refreshTokens(customUserDetails, expiredToken))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("리프레시 토큰이 만료되었거나 유효하지 않습니다");

        verify(jwtUtil).validateToken(expiredToken, false);
        verify(jwtUtil, never()).extractRefreshToken(anyString());
        verify(jwtAuthManager, never()).createTokens(any());
    }

    @Test
    @DisplayName("토큰의 사용자 정보와 현재 사용자 불일치 - 예외 발생")
    void refreshTokens_fail_userMismatch() {
        // given
        User mockUser = User.builder().id(1L).email("test@test.com").password("password").build();

        CustomUserDetails customUserDetails =
                new CustomUserDetails(
                        mockUser,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        customUserDetails.setId(1L);

        String refreshToken = "valid_token_but_different_user";

        Claims claims = mock(Claims.class);
        when(claims.get("userId", Double.class)).thenReturn(2.0); // 다른 사용자 ID

        when(tokenBlacklistManager.isBlacklisted(refreshToken)).thenReturn(false);
        when(jwtUtil.validateToken(refreshToken, false)).thenReturn(true);
        when(jwtUtil.extractRefreshToken(refreshToken)).thenReturn(claims);

        // when & then
        assertThatThrownBy(() -> tokenRefreshService.refreshTokens(customUserDetails, refreshToken))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("토큰 사용자 정보와 일치하지 않습니다");

        verify(jwtUtil).extractRefreshToken(refreshToken);
        verify(jwtAuthManager, never()).createTokens(any());
    }

    @Test
    @DisplayName("토큰 갱신 후 새로운 토큰 페어 반환 확인")
    void refreshTokens_newTokensReturned() {
        // given
        User mockUser =
                User.builder()
                        .id(1L)
                        .email("test@test.com")
                        .password("password")
                        .nickname("테스터")
                        .role(Roles.ROLE_USER)
                        .isFirstLogin(false)
                        .build();

        CustomUserDetails customUserDetails =
                new CustomUserDetails(
                        mockUser,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        customUserDetails.setId(1L);
        customUserDetails.setNickname("테스터");
        customUserDetails.setFirstLogin(false);

        String oldRefreshToken = "old_refresh_token";

        Claims claims = mock(Claims.class);
        when(claims.get("userId", Double.class)).thenReturn(1.0);

        JwtAuthManager.TokenPair newTokenPair =
                new JwtAuthManager.TokenPair("brand_new_access_token", "brand_new_refresh_token");

        when(tokenBlacklistManager.isBlacklisted(oldRefreshToken)).thenReturn(false);
        when(jwtUtil.validateToken(oldRefreshToken, false)).thenReturn(true);
        when(jwtUtil.extractRefreshToken(oldRefreshToken)).thenReturn(claims);
        when(jwtAuthManager.createTokens(any())).thenReturn(newTokenPair);

        // when
        LoginRes result = tokenRefreshService.refreshTokens(customUserDetails, oldRefreshToken);

        // then
        assertThat(result.accessToken()).isEqualTo("brand_new_access_token");
        assertThat(result.refreshToken()).isEqualTo("brand_new_refresh_token");
        assertThat(result.accessToken()).isNotEqualTo(oldRefreshToken);
        assertThat(result.nickname()).isEqualTo("테스터");
        assertThat(result.isFirstLogin()).isFalse();
    }

    @Test
    @DisplayName("검증 순서 확인 - 블랙리스트 먼저, 유효성 두번째")
    void refreshTokens_validationOrder() {
        // given
        User mockUser = User.builder().id(1L).email("test@test.com").password("password").build();

        CustomUserDetails customUserDetails =
                new CustomUserDetails(
                        mockUser,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        customUserDetails.setId(1L);

        String refreshToken = "test_token";

        when(tokenBlacklistManager.isBlacklisted(refreshToken)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> tokenRefreshService.refreshTokens(customUserDetails, refreshToken))
                .isInstanceOf(RuntimeException.class);

        // 블랙리스트 체크는 실행되었지만, 유효성 검사는 실행되지 않아야 함
        verify(tokenBlacklistManager).isBlacklisted(refreshToken);
        verify(jwtUtil, never()).validateToken(anyString(), anyBoolean());
    }

    @Test
    @DisplayName("Integer 타입 userId도 정상 처리")
    void refreshTokens_integerUserId_success() {
        // given
        User mockUser =
                User.builder()
                        .id(5L)
                        .email("test@test.com")
                        .password("password")
                        .nickname("테스터")
                        .build();

        CustomUserDetails customUserDetails =
                new CustomUserDetails(
                        mockUser,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        customUserDetails.setId(5L);
        customUserDetails.setNickname("테스터");

        String refreshToken = "valid_token";

        Claims claims = mock(Claims.class);
        when(claims.get("userId", Double.class)).thenReturn(5.0); // Double로 저장

        JwtAuthManager.TokenPair newTokenPair = new JwtAuthManager.TokenPair("access", "refresh");

        when(tokenBlacklistManager.isBlacklisted(refreshToken)).thenReturn(false);
        when(jwtUtil.validateToken(refreshToken, false)).thenReturn(true);
        when(jwtUtil.extractRefreshToken(refreshToken)).thenReturn(claims);
        when(jwtAuthManager.createTokens(any())).thenReturn(newTokenPair);

        // when
        LoginRes result = tokenRefreshService.refreshTokens(customUserDetails, refreshToken);

        // then
        assertThat(result.userId()).isEqualTo(5L);
    }
}
