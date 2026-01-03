package BookPick.mvp.domain.auth.controller;

import BookPick.mvp.domain.auth.dto.LoginRes;
import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.auth.service.TokenRefreshService;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.RefreshTokenCookieManager;
import BookPick.mvp.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;

@WebMvcTest(value = TokenRefreshController.class,
    excludeAutoConfiguration = {
        SecurityAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
    })
@DisplayName("토큰 갱신 컨트롤러 테스트")
class TokenRefreshControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RefreshTokenCookieManager refreshTokenCookieManager;

    @MockBean
    private TokenRefreshService tokenRefreshService;

    @MockBean
    private BookPick.mvp.global.util.JwtUtil jwtUtil;

    @MockBean
    private BookPick.mvp.global.config.JwtFilter jwtFilter;

    @Test
    @DisplayName("정상 토큰 갱신 성공")
    @WithMockUser
    void refreshToken_success() throws Exception {
        // given
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .nickname("테스터")
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        userDetails.setId(1L);
        userDetails.setNickname("테스터");

        String oldRefreshToken = "old_refresh_token";
        LoginRes newTokens = new LoginRes(
                1L,
                "test@test.com",
                "테스터",
                "자기소개",
                "profile.jpg",
                false,
                "new_access_token",
                "new_refresh_token"
        );

        when(refreshTokenCookieManager.getRefreshTokenFromCookie(any(HttpServletRequest.class)))
                .thenReturn(oldRefreshToken);
        when(tokenRefreshService.refreshTokens(any(CustomUserDetails.class), eq(oldRefreshToken)))
                .thenReturn(newTokens);

        // when & then
        mockMvc.perform(post("/api/v1/auth/token/refresh")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("TOKEN_REFERSH_SUCCESS"))
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.accessToken").value("new_access_token"))
                .andExpect(jsonPath("$.data.refreshToken").doesNotExist()); // refresh token은 응답에 없어야 함

        verify(refreshTokenCookieManager).getRefreshTokenFromCookie(any(HttpServletRequest.class));
        verify(tokenRefreshService).refreshTokens(any(CustomUserDetails.class), eq(oldRefreshToken));
        verify(refreshTokenCookieManager).addRefreshTokenCookie(any(HttpServletResponse.class), eq("new_refresh_token"));
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 리프레시 토큰 없음")
    @WithMockUser
    void refreshToken_fail_noRefreshToken() throws Exception {
        // given
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        userDetails.setId(1L);

        when(refreshTokenCookieManager.getRefreshTokenFromCookie(any(HttpServletRequest.class)))
                .thenReturn(null);
        when(tokenRefreshService.refreshTokens(any(CustomUserDetails.class), isNull()))
                .thenThrow(new RuntimeException("리프레시 토큰이 없습니다."));

        // when & then
        mockMvc.perform(post("/api/v1/auth/token/refresh")
                        .with(user(userDetails)))
                .andExpect(status().is5xxServerError());

        verify(refreshTokenCookieManager).getRefreshTokenFromCookie(any(HttpServletRequest.class));
        verify(refreshTokenCookieManager, never()).addRefreshTokenCookie(any(), anyString());
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 만료된 리프레시 토큰")
    @WithMockUser
    void refreshToken_fail_expiredToken() throws Exception {
        // given
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        userDetails.setId(1L);

        String expiredToken = "expired_refresh_token";

        when(refreshTokenCookieManager.getRefreshTokenFromCookie(any(HttpServletRequest.class)))
                .thenReturn(expiredToken);
        when(tokenRefreshService.refreshTokens(any(CustomUserDetails.class), eq(expiredToken)))
                .thenThrow(new RuntimeException("리프레시 토큰이 만료되었거나 유효하지 않습니다."));

        // when & then
        mockMvc.perform(post("/api/v1/auth/token/refresh")
                        .with(user(userDetails)))
                .andExpect(status().is5xxServerError());

        verify(refreshTokenCookieManager, never()).addRefreshTokenCookie(any(), anyString());
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 블랙리스트 토큰")
    @WithMockUser
    void refreshToken_fail_blacklistedToken() throws Exception {
        // given
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        userDetails.setId(1L);

        String blacklistedToken = "blacklisted_token";

        when(refreshTokenCookieManager.getRefreshTokenFromCookie(any(HttpServletRequest.class)))
                .thenReturn(blacklistedToken);
        when(tokenRefreshService.refreshTokens(any(CustomUserDetails.class), eq(blacklistedToken)))
                .thenThrow(new RuntimeException("유효하지 않은 리프레시 토큰입니다."));

        // when & then
        mockMvc.perform(post("/api/v1/auth/token/refresh")
                        .with(user(userDetails)))
                .andExpect(status().is5xxServerError());

        verify(refreshTokenCookieManager, never()).addRefreshTokenCookie(any(), anyString());
    }

    @Test
    @DisplayName("미인증 사용자 토큰 갱신 시도")
    void refreshToken_fail_notAuthenticated() throws Exception {
        // when & then
        mockMvc.perform(post("/api/v1/auth/token/refresh"))
                .andExpect(status().isUnauthorized());

        verify(refreshTokenCookieManager, never()).getRefreshTokenFromCookie(any());
        verify(tokenRefreshService, never()).refreshTokens(any(), anyString());
    }

    @Test
    @DisplayName("새 리프레시 토큰 쿠키 설정 확인")
    @WithMockUser
    void refreshToken_newRefreshTokenCookieSet() throws Exception {
        // given
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        userDetails.setId(1L);

        String oldToken = "old_token";
        String newRefreshToken = "brand_new_refresh_token";
        LoginRes newTokens = new LoginRes(
                1L,
                "test@test.com",
                "테스터",
                "자기소개",
                "profile.jpg",
                false,
                "new_access_token",
                newRefreshToken
        );

        when(refreshTokenCookieManager.getRefreshTokenFromCookie(any(HttpServletRequest.class)))
                .thenReturn(oldToken);
        when(tokenRefreshService.refreshTokens(any(CustomUserDetails.class), eq(oldToken)))
                .thenReturn(newTokens);

        // when
        mockMvc.perform(post("/api/v1/auth/token/refresh")
                        .with(user(userDetails)))
                .andExpect(status().isOk());

        // then
        verify(refreshTokenCookieManager).addRefreshTokenCookie(
                any(HttpServletResponse.class),
                eq(newRefreshToken)
        );
    }

    @Test
    @DisplayName("응답에서 리프레시 토큰 제거 확인")
    @WithMockUser
    void refreshToken_refreshTokenNotInResponse() throws Exception {
        // given
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        userDetails.setId(1L);

        LoginRes newTokens = new LoginRes(
                1L,
                "test@test.com",
                "테스터",
                "자기소개",
                "profile.jpg",
                false,
                "new_access_token",
                "new_refresh_token"
        );

        when(refreshTokenCookieManager.getRefreshTokenFromCookie(any(HttpServletRequest.class)))
                .thenReturn("old_token");
        when(tokenRefreshService.refreshTokens(any(CustomUserDetails.class), anyString()))
                .thenReturn(newTokens);

        // when & then
        mockMvc.perform(post("/api/v1/auth/token/refresh")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").doesNotExist());
    }

    @Test
    @DisplayName("토큰 갱신 서비스 호출 확인")
    @WithMockUser
    void refreshToken_serviceInvoked() throws Exception {
        // given
        User mockUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(
                mockUser,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        userDetails.setId(1L);

        String refreshToken = "refresh_token";
        LoginRes newTokens = new LoginRes(1L, "test@test.com", "테스터", "자기소개", "profile.jpg", false, "access", "refresh");

        when(refreshTokenCookieManager.getRefreshTokenFromCookie(any(HttpServletRequest.class)))
                .thenReturn(refreshToken);
        when(tokenRefreshService.refreshTokens(any(CustomUserDetails.class), eq(refreshToken)))
                .thenReturn(newTokens);

        // when
        mockMvc.perform(post("/api/v1/auth/token/refresh")
                        .with(user(userDetails)))
                .andExpect(status().isOk());

        // then
        verify(tokenRefreshService, times(1)).refreshTokens(any(CustomUserDetails.class), eq(refreshToken));
    }
}
