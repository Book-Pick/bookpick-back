package BookPick.mvp.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final JwtFilter jwtFilter;

    @Bean  // 이 메서드가 반환하는 객체(SecurityFilterChain)를 스프링 빈으로 등록
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // CSRF(Cross Site Request Forgery, 사이트 간 위조 요청) 방어 기능 비활성화
            // REST API 서버에서는 세션을 사용하지 않으므로 보통 꺼둡니다.
            .csrf(csrf -> csrf.disable())

            // 세션 관리 정책 설정
            // STATELESS = 서버가 세션을 생성하거나 사용하지 않음 (JWT 기반 인증 전제 조건)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // URL 별 접근 권한 규칙 정의
            .authorizeHttpRequests(auth -> auth
                // 회원가입, 로그인 요청은 인증 없이 누구나 접근 가능
                .requestMatchers("/api/auth/signup", "/api/auth/login", "/api/auth/logout").permitAll()
                .requestMatchers("/api/users/*/preferences").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                // 나머지 모든 요청은 인증된 사용자만 접근 가능
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

            // 위 설정으로 SecurityFilterChain 객체 생성
            .build();
    }
}
