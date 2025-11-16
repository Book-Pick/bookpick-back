package BookPick.mvp.domain.auth.util.Manager.login.jwt;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCookieManager {


    private static final String COOKIE_NAME = "refreshToken";
    private static final int MAX_AGE_SECONDS = 60 * 60 * 24 * 7; // 7일

    /**
     * HttpOnly, Secure 쿠키로 리프레시 토큰 세팅
     */
    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshCookie = new Cookie(COOKIE_NAME, refreshToken);
        refreshCookie.setHttpOnly(true);  // JS에서 접근 불가
        refreshCookie.setSecure(true);    // HTTPS 환경에서만 전송
        refreshCookie.setPath("/");       // 전체 경로에서 사용
        refreshCookie.setMaxAge(MAX_AGE_SECONDS);
        response.addCookie(refreshCookie);
    }

    /**
     * 로그아웃 시 쿠키 제거
     */
    public void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshCookie = new Cookie(COOKIE_NAME, null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);  // 즉시 삭제
        response.addCookie(refreshCookie);
    }


    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null; // 없으면 null 반환
    }
}
