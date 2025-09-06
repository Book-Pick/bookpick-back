//package BookPick.mvp.global.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private static final String[] SWAGGER_WHITELIST = {
//            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
//    };
//
//    private static final String[] STATIC_RESOURCES = {
//            "/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico"
//    };
//
//    // 뷰 라우트(비로그인 허용 페이지)
//    private static final String[] VIEW_PUBLIC = {
//            "/", "/item/list", "/member/login", "/member/signup", "/member/add",
//            "/member/logout", "/member/logout/jwt" // 필요시 POST도 허용
//    };
//
//    @Bean
//    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable());
//        http.cors(Customizer.withDefaults());
//
//        // ✅ JWT 필터는 UsernamePasswordAuthenticationFilter 앞에
//        http.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        // JWT 사용 → 세션 미사용
//        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        http.authorizeHttpRequests(auth -> auth
//                // 프리플라이트
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                // 정적/스웨거/공개 뷰
//                .requestMatchers(STATIC_RESOURCES).permitAll()
//                .requestMatchers(SWAGGER_WHITELIST).permitAll()
//                .requestMatchers(VIEW_PUBLIC).permitAll()
//                // API는 인증 필수
//                .requestMatchers("/api/**").authenticated()
//                // 나머지는 필요에 따라: 뷰를 전부 공개하려면 permitAll, 내부페이지면 authenticated
//                .anyRequest().permitAll()
//        );
//
//        // 폼 로그인 페이지(뷰용): 페이지 자체는 permitAll로 열려 있으니 유지 가능
//        http.formLogin(form -> form
//                .loginPage("/member/login")
//                .defaultSuccessUrl("/item/list", true)
//        );
//
//        http.logout(logout -> logout
//                .logoutUrl("/member/logout")
//                .logoutSuccessUrl("/item/list")
//        );
//
//        return http.build();
//    }
//}
