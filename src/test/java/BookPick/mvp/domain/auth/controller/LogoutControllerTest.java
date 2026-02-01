package BookPick.mvp.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.auth.exception.NotAuthenticateUser;
import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.auth.service.LogoutService;
import BookPick.mvp.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        value = LogoutController.class,
        excludeAutoConfiguration = {
            SecurityAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class,
            JpaRepositoriesAutoConfiguration.class
        })
@DisplayName("로그아웃 컨트롤러 테스트")
class LogoutControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private LogoutService logoutService;

    @MockBean private BookPick.mvp.global.util.JwtUtil jwtUtil;

    @MockBean private BookPick.mvp.global.config.JwtFilter jwtFilter;

    @Test
    @DisplayName("정상 로그아웃 성공")
    @WithMockUser
    void logout_success() throws Exception {
        // given
        User mockUser =
                User.builder()
                        .id(1L)
                        .email("test@test.com")
                        .password("password")
                        .role(Roles.ROLE_USER)
                        .build();

        CustomUserDetails userDetails =
                new CustomUserDetails(
                        mockUser,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        userDetails.setId(1L);

        doNothing()
                .when(logoutService)
                .logout(any(), any(HttpServletRequest.class), any(HttpServletResponse.class));

        // when & then
        mockMvc.perform(post("/api/v1/auth/logout").with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("LOGOUT_SUCCESS"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(logoutService)
                .logout(any(), any(HttpServletRequest.class), any(HttpServletResponse.class));
    }

    @Test
    @DisplayName("미인증 사용자 로그아웃 시도 - 예외 발생")
    void logout_fail_notAuthenticated() throws Exception {
        // given
        doThrow(new NotAuthenticateUser())
                .when(logoutService)
                .logout(any(), any(HttpServletRequest.class), any(HttpServletResponse.class));

        // when & then
        mockMvc.perform(post("/api/v1/auth/logout")).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그아웃 서비스 호출 확인")
    @WithMockUser
    void logout_serviceInvoked() throws Exception {
        // given
        User mockUser = User.builder().id(1L).email("test@test.com").password("password").build();

        CustomUserDetails userDetails =
                new CustomUserDetails(
                        mockUser,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        userDetails.setId(1L);

        doNothing()
                .when(logoutService)
                .logout(any(), any(HttpServletRequest.class), any(HttpServletResponse.class));

        // when
        mockMvc.perform(post("/api/v1/auth/logout").with(user(userDetails)))
                .andExpect(status().isOk());

        // then
        verify(logoutService, times(1))
                .logout(any(), any(HttpServletRequest.class), any(HttpServletResponse.class));
    }

    @Test
    @DisplayName("로그아웃 응답 형식 확인")
    @WithMockUser
    void logout_responseFormat() throws Exception {
        // given
        User mockUser = User.builder().id(1L).email("test@test.com").password("password").build();

        CustomUserDetails userDetails =
                new CustomUserDetails(
                        mockUser,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        userDetails.setId(1L);

        doNothing()
                .when(logoutService)
                .logout(any(), any(HttpServletRequest.class), any(HttpServletResponse.class));

        // when & then
        mockMvc.perform(post("/api/v1/auth/logout").with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("여러 사용자 연속 로그아웃")
    @WithMockUser
    void logout_multipleUsers() throws Exception {
        // given
        User user1 = User.builder().id(1L).email("user1@test.com").password("password").build();

        User user2 = User.builder().id(2L).email("user2@test.com").password("password").build();

        CustomUserDetails userDetails1 =
                new CustomUserDetails(
                        user1, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        userDetails1.setId(1L);

        CustomUserDetails userDetails2 =
                new CustomUserDetails(
                        user2, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        userDetails2.setId(2L);

        doNothing()
                .when(logoutService)
                .logout(any(), any(HttpServletRequest.class), any(HttpServletResponse.class));

        // when & then
        mockMvc.perform(post("/api/v1/auth/logout").with(user(userDetails1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/logout").with(user(userDetails2)))
                .andExpect(status().isOk());

        verify(logoutService, times(2))
                .logout(any(), any(HttpServletRequest.class), any(HttpServletResponse.class));
    }
}
