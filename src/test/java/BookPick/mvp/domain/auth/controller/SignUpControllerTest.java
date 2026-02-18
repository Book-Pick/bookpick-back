package BookPick.mvp.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import BookPick.mvp.domain.auth.dto.SignReq;
import BookPick.mvp.domain.auth.dto.SignRes;
import BookPick.mvp.domain.auth.exception.DuplicateEmailException;
import BookPick.mvp.domain.auth.service.SignUpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        value = SignUpController.class,
        excludeAutoConfiguration = {
            SecurityAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class,
            JpaRepositoriesAutoConfiguration.class
        })
@DisplayName("회원가입 컨트롤러 테스트")
class SignUpControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean private SignUpService authService;

    @MockBean private BookPick.mvp.global.util.JwtUtil jwtUtil;

    @MockBean private BookPick.mvp.global.config.JwtFilter jwtFilter;

    @Test
    @DisplayName("정상 회원가입 성공")
    void signUp_success() throws Exception {
        // given
        SignReq req = new SignReq("test@test.com", "password123");
        SignRes res = new SignRes(1L);

        when(authService.signUp(any(SignReq.class))).thenReturn(res);

        // when & then
        mockMvc.perform(
                        post("/api/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("REGISTER_SUCCESS"))
                .andExpect(jsonPath("$.data.userId").value(1L));

        verify(authService).signUp(any(SignReq.class));
    }

    @Test
    @DisplayName("이메일 중복 - 예외 발생")
    void signUp_fail_duplicateEmail() throws Exception {
        // given
        SignReq req = new SignReq("duplicate@test.com", "password123");

        when(authService.signUp(any(SignReq.class))).thenThrow(new DuplicateEmailException());

        // when & then
        mockMvc.perform(
                        post("/api/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(authService).signUp(any(SignReq.class));
    }

    @Test
    @DisplayName("이메일 형식 검증 - 빈 이메일")
    void signUp_fail_emptyEmail() throws Exception {
        // given
        SignReq req = new SignReq("", "password123");

        // when & then
        mockMvc.perform(
                        post("/api/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).signUp(any(SignReq.class));
    }

    @Test
    @DisplayName("이메일 형식 검증 - 잘못된 형식")
    void signUp_fail_invalidEmailFormat() throws Exception {
        // given
        SignReq req = new SignReq("invalid-email", "password123");

        // when & then
        mockMvc.perform(
                        post("/api/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).signUp(any(SignReq.class));
    }

    @Test
    @DisplayName("비밀번호 검증 - 빈 비밀번호")
    void signUp_fail_emptyPassword() throws Exception {
        // given
        SignReq req = new SignReq("test@test.com", "");

        // when & then
        mockMvc.perform(
                        post("/api/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).signUp(any(SignReq.class));
    }

    @Test
    @DisplayName("요청 바디 없음 - 예외 발생")
    void signUp_fail_noRequestBody() throws Exception {
        // when & then
        mockMvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(authService, never()).signUp(any(SignReq.class));
    }

    @Test
    @DisplayName("Content-Type 누락 - 예외 발생")
    void signUp_fail_noContentType() throws Exception {
        // given
        SignReq req = new SignReq("test@test.com", "password123");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup").content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnsupportedMediaType());

        verify(authService, never()).signUp(any(SignReq.class));
    }

    @Test
    @DisplayName("여러 사용자 연속 회원가입")
    void signUp_multipleUsers_success() throws Exception {
        // given
        SignReq req1 = new SignReq("user1@test.com", "password1");
        SignReq req2 = new SignReq("user2@test.com", "password2");

        SignRes res1 = new SignRes(1L);
        SignRes res2 = new SignRes(2L);

        when(authService.signUp(any(SignReq.class))).thenReturn(res1).thenReturn(res2);

        // when & then
        mockMvc.perform(
                        post("/api/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1L));

        mockMvc.perform(
                        post("/api/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(2L));

        verify(authService, times(2)).signUp(any(SignReq.class));
    }
}
