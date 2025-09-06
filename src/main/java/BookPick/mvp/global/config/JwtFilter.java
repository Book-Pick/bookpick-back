//package BookPick.mvp.global.config;
//
//import com.apple.shop.domain.member.service.MyUserDetailsService.CustomUser;
//import com.apple.shop.global.util.JwtUtil;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Arrays;
//
//public class JwtFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        Cookie jwtCookie = findCookie(request, "jwt");
//        if (jwtCookie == null || isAuthenticatedAlready()) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            // 1) 파싱/검증
//            Claims claim = JwtUtil.extractToken(jwtCookie.getValue());
//
//            // 2) 인증 세팅
//            String[] arr = claim.get("authorities").toString().split(",");
//            var authorities = Arrays.stream(arr).map(SimpleGrantedAuthority::new).toList();
//
//            String username = String.valueOf(claim.get("username"));
//            CustomUser principal = new CustomUser(username, "", authorities);
//            principal.displayName = String.valueOf(claim.get("displayName"));
//
//            var authToken = new UsernamePasswordAuthenticationToken(principal, null, authorities);
//            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//
//            filterChain.doFilter(request, response);
//        } catch (ExpiredJwtException e) {
//            // 만료 토큰: 쿠키 제거
//            clearJwtCookie(response);
//            // API 경로면 401, 뷰/정적은 계속 통과
//            if (isApi(request)) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            }
//            filterChain.doFilter(request, response);
//        } catch (JwtException | IllegalArgumentException e) {
//            // 서명 불일치/손상 등: 쿠키 제거 후 동일 처리
//            clearJwtCookie(response);
//            if (isApi(request)) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            }
//            filterChain.doFilter(request, response);
//        }
//    }
//
//    private boolean isAuthenticatedAlready() {
//        return SecurityContextHolder.getContext().getAuthentication() != null;
//    }
//
//    private boolean isApi(HttpServletRequest req) {
//        String uri = req.getRequestURI();
//        return uri != null && uri.startsWith("/api/");
//    }
//
//    private Cookie findCookie(HttpServletRequest request, String name) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null) return null;
//        for (Cookie c : cookies) if (name.equals(c.getName())) return c;
//        return null;
//    }
//
//    private void clearJwtCookie(HttpServletResponse res) {
//        Cookie c = new Cookie("jwt", null);
//        c.setPath("/");
//        c.setMaxAge(0);
//        c.setHttpOnly(true);
//        res.addCookie(c);
//    }
//}
