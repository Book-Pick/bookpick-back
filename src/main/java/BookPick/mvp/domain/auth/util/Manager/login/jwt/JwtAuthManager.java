package BookPick.mvp.domain.auth.util.Manager.login.jwt;

import BookPick.mvp.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthManager {
    private final JwtUtil jwtUtil;

    // 1. 토큰 생성
    public TokenPair createTokens(Authentication token) {
        String accessToken = jwtUtil.createAccessToken(token);
        String refreshToken = jwtUtil.createRefreshToken(token);

        return TokenPair.from(accessToken, refreshToken);
    }

    public record TokenPair(String accessToken, String refreshToken) {
        public static TokenPair from(String accessToken, String refreshToken) {
            return new TokenPair(accessToken, refreshToken);
        }
    }
}
