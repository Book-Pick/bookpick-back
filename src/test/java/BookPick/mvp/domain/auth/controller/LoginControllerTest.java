package BookPick.mvp.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import BookPick.mvp.domain.auth.dto.LoginReq;
import BookPick.mvp.domain.auth.dto.LoginRes;
import BookPick.mvp.domain.auth.exception.InvalidLoginException;
import BookPick.mvp.domain.auth.service.LoginService;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.RefreshTokenCookieManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@Transactional
@DisplayName("로그인 컨트롤러 테스트")
class LoginControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean private LoginService loginService;

    @MockBean private RefreshTokenCookieManager refreshTokenCookieManager;

    @Test
    @DisplayName("정상 로그인 성공 - 첫 로그인")
    void login_success_firstLogin() throws Exception {
        // given
        LoginReq req = new LoginReq("test@test.com", "password123");
        LoginRes loginRes =
                new LoginRes(
                        1L,
                        "test@test.com",
                        "테스터",
                        "자기소개",
                        "profile.jpg",
                        true,
                        "access_token",
                        "refresh_token");

        when(loginService.login(any(LoginReq.class), any(HttpServletRequest.class)))
                .thenReturn(loginRes);

        // when & then
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("LOGIN_SUCCESS"))
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.email").value("test@test.com"))
                .andExpect(jsonPath("$.data.nickname").value("테스터"))
                .andExpect(jsonPath("$.data.accessToken").value("access_token"))
                .andExpect(jsonPath("$.data.isFirstLogin").value(true))
                .andExpect(
                        jsonPath("$.data.refreshToken").doesNotExist()); // refresh token은 응답에 없어야 함

        verify(loginService).login(any(LoginReq.class), any(HttpServletRequest.class));
        verify(refreshTokenCookieManager)
                .addRefreshTokenCookie(any(HttpServletResponse.class), eq("refresh_token"));
    }

    @Test
    @DisplayName("정상 로그인 성공 - 재로그인")
    void login_success_notFirstLogin() throws Exception {
        // given
        LoginReq req = new LoginReq("test@test.com", "password123");
        LoginRes loginRes =
                new LoginRes(
                        1L,
                        "test@test.com",
                        "테스터",
                        "자기소개",
                        "profile.jpg",
                        false,
                        "access_token",
                        "refresh_token");

        when(loginService.login(any(LoginReq.class), any(HttpServletRequest.class)))
                .thenReturn(loginRes);

        // when & then
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isFirstLogin").value(false));

        verify(refreshTokenCookieManager)
                .addRefreshTokenCookie(any(HttpServletResponse.class), eq("refresh_token"));
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_fail_wrongPassword() throws Exception {
        // given
        LoginReq req = new LoginReq("test@test.com", "wrongPassword");

        when(loginService.login(any(LoginReq.class), any(HttpServletRequest.class)))
                .thenThrow(new InvalidLoginException());

        // when & then
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());

        verify(loginService).login(any(LoginReq.class), any(HttpServletRequest.class));
        verify(refreshTokenCookieManager, never()).addRefreshTokenCookie(any(), anyString());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자")
    void login_fail_userNotFound() throws Exception {
        // given
        LoginReq req = new LoginReq("notexist@test.com", "password123");

        when(loginService.login(any(LoginReq.class), any(HttpServletRequest.class)))
                .thenThrow(new InvalidLoginException());

        // when & then
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());

        verify(refreshTokenCookieManager, never()).addRefreshTokenCookie(any(), anyString());
    }

    @Test
    @DisplayName("이메일 형식 검증 - 빈 이메일")
    void login_fail_emptyEmail() throws Exception {
        // given
        LoginReq req = new LoginReq("", "password123");

        // when & then
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(loginService, never()).login(any(LoginReq.class), any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("이메일 형식 검증 - 잘못된 형식")
    void login_fail_invalidEmailFormat() throws Exception {
        // given
        LoginReq req = new LoginReq("invalid-email", "password123");

        // when & then
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(loginService, never()).login(any(LoginReq.class), any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("비밀번호 검증 - 빈 비밀번호")
    void login_fail_emptyPassword() throws Exception {
        // given
        LoginReq req = new LoginReq("test@test.com", "");

        // when & then
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(loginService, never()).login(any(LoginReq.class), any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("요청 바디 없음 - 예외 발생")
    void login_fail_noRequestBody() throws Exception {
        // when & then
        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(loginService, never()).login(any(LoginReq.class), any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("리프레시 토큰 쿠키 설정 확인")
    void login_refreshTokenCookie_set() throws Exception {
        // given
        LoginReq req = new LoginReq("test@test.com", "password123");
        LoginRes loginRes =
                new LoginRes(
                        1L,
                        "test@test.com",
                        "테스터",
                        "자기소개",
                        "profile.jpg",
                        false,
                        "access_token",
                        "refresh_token_value");

        when(loginService.login(any(LoginReq.class), any(HttpServletRequest.class)))
                .thenReturn(loginRes);

        // when
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        // then
        verify(refreshTokenCookieManager)
                .addRefreshTokenCookie(any(HttpServletResponse.class), eq("refresh_token_value"));
    }

    @Test
    @DisplayName("응답에서 리프레시 토큰 제거 확인")
    void login_refreshTokenNotInResponse() throws Exception {
        // given
        LoginReq req = new LoginReq("test@test.com", "password123");
        LoginRes loginRes =
                new LoginRes(
                        1L,
                        "test@test.com",
                        "테스터",
                        "자기소개",
                        "profile.jpg",
                        false,
                        "access_token",
                        "refresh_token");

        when(loginService.login(any(LoginReq.class), any(HttpServletRequest.class)))
                .thenReturn(loginRes);

        // when & then
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").doesNotExist());
    }
}
