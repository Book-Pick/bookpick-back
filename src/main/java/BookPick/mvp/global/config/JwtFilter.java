package BookPick.mvp.global.config;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer";


    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {


        // 이미 인증된 상태면 패스
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveAccessToken(request);         // 토큰있는지 문자열 체크
        if (token == null) { // 토큰 없으면 그냥 통과
            filterChain.doFilter(request, response);        // 넘어가요
            return;
        }

        Claims claims = JwtUtil.extractToken(token);    // 토큰 까기 (토큰 진위여부 검증 해당 메서드에서 진행)

        Long userId = claims.get("userId", Number.class).longValue();
        String email = claims.get("email").toString();


        var authorities = Arrays.stream(
                claims.get("authorities").toString().split(",")
        ).map(SimpleGrantedAuthority::new).toList();


        CustomUserDetails customUserDetails = CustomUserDetails.fromJwt(userId, email, authorities);

        var auth = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities()
        );
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));     // 추가 정보 저장 (어디서 접속했는지)

        SecurityContextHolder.getContext().setAuthentication(auth);


        filterChain.doFilter(request, response);
    }


    private String resolveAccessToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER)) {
            return header.substring(BEARER.length()).trim();
        }
        return null;
    }

}
