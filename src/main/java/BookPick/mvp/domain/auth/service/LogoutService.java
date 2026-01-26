package BookPick.mvp.domain.auth.service;

import BookPick.mvp.domain.auth.exception.NotAuthenticateUser;
import BookPick.mvp.domain.auth.util.Manager.login.jwt.TokenBlacklistManager;
import BookPick.mvp.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import BookPick.mvp.domain.auth.exception.JwtTokenExpiredException;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService {

    private final JwtUtil jwtUtil;
    private final TokenBlacklistManager tokenBlacklistManager;


    public void logout(CustomUserDetails currentUser, HttpServletRequest request, HttpServletResponse response) {


        // 1. 인증 상태 검증
        if (currentUser == null) {
            throw new NotAuthenticateUser();
        }


        // 2. 쿠키 검증
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return;


        // 3. 리프레시 토큰 검증
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {

                // 3.1 리프레시 토큰 획득
                String refreshToken = cookie.getValue();

                if (refreshToken == null || refreshToken.isEmpty()) return;


                int a=1;
                // 3.2 클레임 토큰 획득
                Claims claims;
                int b=1;

                try {
                    claims = jwtUtil.extractRefreshToken(refreshToken);
                } catch (Exception e) {
                    throw new JwtTokenExpiredException();
                }

                // 3.3 토큰의 userId와 현재 인증된 user 비교
                Long tokenUserId;
                try {
                    tokenUserId = claims.get("userId", Number.class).longValue();
                } catch (Exception e) {
                    throw new AccessDeniedException("Invalid token structure");
                }


                // 3.4 jti 및 만료시간 계산 후 블랙리스트 추가
                String jti = claims.getId();
                long expMillis = claims.getExpiration().getTime() - System.currentTimeMillis();
                if (jti != null && expMillis > 0) {
                    tokenBlacklistManager.add(jti, Instant.now().plusMillis(expMillis));
                }


                // 3.5 클라이언트 쿠키 제거
                Cookie del = new Cookie("refreshToken", null);
                del.setHttpOnly(true);
                del.setSecure(true);
                del.setPath("/");
                del.setMaxAge(0);
                response.addCookie(del);


                // 3.6 쿠키 더 순회하지 않고 종료
                break;
            }
        }
    }
}

