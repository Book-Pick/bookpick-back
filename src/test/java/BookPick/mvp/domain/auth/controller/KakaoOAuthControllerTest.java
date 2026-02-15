package BookPick.mvp.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import BookPick.mvp.domain.auth.dto.LoginRes;
import BookPick.mvp.domain.auth.dto.OAuthTokenRequest;
import BookPick.mvp.domain.auth.service.KakaoOAuthService;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.RefreshTokenCookieManager;
import BookPick.mvp.domain.auth.util.OAuthTempCodeStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
@DisplayName("카카오 OAuth 컨트롤러 테스트")
class KakaoOAuthControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean private KakaoOAuthService kakaoOAuthService;

    @MockBean private RefreshTokenCookieManager refreshTokenCookieManager;

    @MockBean private OAuthTempCodeStore tempCodeStore;

    @Nested
    @DisplayName("GET /api/v1/oauth/kakao - 카카오 인증 URL 조회")
    class GetKakaoAuthUrl {

        @Test
        @DisplayName("성공 - 카카오 인증 URL 반환")
        void success() throws Exception {
            // given
            String expectedUrl = "https://kauth.kakao.com/oauth/authorize?client_id=test&redirect_uri=http://test.com/callback&response_type=code";
            when(kakaoOAuthService.getKakaoAuthUrl()).thenReturn(expectedUrl);

            // when & then
            mockMvc.perform(get("/api/v1/oauth/kakao"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.authUrl").value(expectedUrl));

            verify(kakaoOAuthService).getKakaoAuthUrl();
        }
    }

    @Nested
    @DisplayName("GET /api/v1/oauth/kakao/callback - 카카오 콜백")
    class KakaoCallback {

        @Test
        @DisplayName("성공 - 프론트엔드로 리다이렉트")
        void success_redirect_to_frontend() throws Exception {
            // given
            String kakaoCode = "kakao_authorization_code";
            String tempCode = "temp-uuid-code";
            LoginRes loginRes = createLoginRes();

            when(kakaoOAuthService.processKakaoCallback(kakaoCode)).thenReturn(loginRes);
            when(tempCodeStore.store(loginRes)).thenReturn(tempCode);

            // when & then
            mockMvc.perform(get("/api/v1/oauth/kakao/callback").param("code", kakaoCode))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection());

            verify(kakaoOAuthService).processKakaoCallback(kakaoCode);
            verify(tempCodeStore).store(loginRes);
        }
    }

    @Nested
    @DisplayName("POST /api/v1/oauth/token - 토큰 교환")
    class ExchangeToken {

        @Test
        @DisplayName("성공 - 임시 코드로 토큰 발급")
        void success() throws Exception {
            // given
            String tempCode = "valid-temp-code";
            OAuthTokenRequest request = new OAuthTokenRequest(tempCode);
            LoginRes loginRes = createLoginRes();

            when(tempCodeStore.consume(tempCode)).thenReturn(Optional.of(loginRes));

            // when & then
            mockMvc.perform(
                            post("/api/v1/oauth/token")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.userId").value(1L))
                    .andExpect(jsonPath("$.data.email").value("test@kakao.com"))
                    .andExpect(jsonPath("$.data.nickname").value("테스터"))
                    .andExpect(jsonPath("$.data.accessToken").value("access_token"))
                    .andExpect(jsonPath("$.data.isFirstLogin").value(true))
                    .andExpect(jsonPath("$.data.refreshToken").doesNotExist());

            verify(tempCodeStore).consume(tempCode);
            verify(refreshTokenCookieManager)
                    .addRefreshTokenCookie(any(HttpServletResponse.class), eq("refresh_token"));
        }

        @Test
        @DisplayName("실패 - 유효하지 않은 임시 코드")
        void fail_invalid_code() throws Exception {
            // given
            String invalidCode = "invalid-code";
            OAuthTokenRequest request = new OAuthTokenRequest(invalidCode);

            when(tempCodeStore.consume(invalidCode)).thenReturn(Optional.empty());

            // when & then
            mockMvc.perform(
                            post("/api/v1/oauth/token")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());

            verify(tempCodeStore).consume(invalidCode);
            verify(refreshTokenCookieManager, never()).addRefreshTokenCookie(any(), anyString());
        }

        @Test
        @DisplayName("실패 - 빈 코드")
        void fail_empty_code() throws Exception {
            // given
            String json = "{\"code\": \"\"}";

            // when & then
            mockMvc.perform(
                            post("/api/v1/oauth/token")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(tempCodeStore, never()).consume(anyString());
        }

        @Test
        @DisplayName("실패 - 요청 바디 없음")
        void fail_no_request_body() throws Exception {
            // when & then
            mockMvc.perform(
                            post("/api/v1/oauth/token")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(tempCodeStore, never()).consume(anyString());
        }

        @Test
        @DisplayName("리프레시 토큰은 응답에 포함되지 않음")
        void refresh_token_not_in_response() throws Exception {
            // given
            String tempCode = "valid-temp-code";
            OAuthTokenRequest request = new OAuthTokenRequest(tempCode);
            LoginRes loginRes = createLoginRes();

            when(tempCodeStore.consume(tempCode)).thenReturn(Optional.of(loginRes));

            // when & then
            mockMvc.perform(
                            post("/api/v1/oauth/token")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.accessToken").exists())
                    .andExpect(jsonPath("$.data.refreshToken").doesNotExist());
        }
    }

    private LoginRes createLoginRes() {
        return new LoginRes(
                1L,
                "test@kakao.com",
                "테스터",
                "자기소개",
                "profile.jpg",
                true,
                "access_token",
                "refresh_token");
    }
}
