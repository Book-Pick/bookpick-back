package BookPick.mvp.global.config;

import BookPick.mvp.domain.auth.service.MyUserDetailsService.*;
import BookPick.mvp.global.ApiResponse;
import BookPick.mvp.global.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

        String token = resolveAccessToken(request);
            if (token == null) { // 토큰 없으면 그냥 통과
                filterChain.doFilter(request, response);
                return;
            }

    try {
            Claims claims = JwtUtil.extractToken(token);

            var authorities = Arrays.stream(
                    claims.get("authorities").toString().split(",")
            ).map(SimpleGrantedAuthority::new).toList();

            var auth = new UsernamePasswordAuthenticationToken(
                    claims.get("email"), null, authorities
            );
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (ExpiredJwtException   e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthenticatedAlready() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    private boolean isApi(HttpServletRequest req) {
        String uri = req.getRequestURI();
        return uri != null && uri.startsWith("/api/");
    }

    private Cookie findCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) if (name.equals(c.getName())) return c;
        return null;
    }

    private void clearJwtCookie(HttpServletResponse res) {
        Cookie c = new Cookie("jwt", null);
        c.setPath("/");
        c.setMaxAge(0);
        c.setHttpOnly(true);
        res.addCookie(c);
    }

    public static class AuthPrincipal extends User {
        private Long id;
        private String email;

        public AuthPrincipal(
                String email,
                Collection<? extends GrantedAuthority> authorities
        ){
            super(email, "",authorities);
        }

    }

    private String resolveAccessToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER)) {
            return header.substring(BEARER.length()).trim();
        }
        return null;
    }

}
