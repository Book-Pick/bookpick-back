package BookPick.mvp.global.util;

import BookPick.mvp.domain.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.access.secret}")
    private String accessSecret;

    @Value("${jwt.access.expiration}")
    private Duration accessExpiration;  // 15m -> Duration 으로 자동 변환

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.refresh.expiration}")
    private Duration refreshExpiration; // 7d -> Duration 으로 자동 변환

    private static final int CLOCK_SKEW_SECONDS = 60;

    private SecretKey getAccessKey() {
        return Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getRefreshKey() {
        return Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }

    /* ====== 토큰 생성 ====== */

    public String createAccessToken(User user) {
        return buildToken(user, getAccessKey(), accessExpiration, "access");
    }

    public String createRefreshToken(User user) {
        return buildToken(user, getRefreshKey(), refreshExpiration, "refresh");
    }

    public Map<String, String> createTokenPair(User user) {
        return Map.of(
                "accessToken", createAccessToken(user),
                "refreshToken", createRefreshToken(user)
        );
    }

    private String buildToken(User user, SecretKey key, Duration ttl, String typ) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .claim("typ", typ)
                .issuedAt(new Date(now))
                .expiration(new Date(now + ttl.toMillis())) // Duration 을 millis 로 변환
                .signWith(key)
                .compact();
    }

    /* ====== 파싱/검증 ====== */

    public Claims parseAccess(String accessToken) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getAccessKey())
                    .clockSkewSeconds(CLOCK_SKEW_SECONDS)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();

            if (!"access".equals(claims.get("typ"))) {
                throw new AuthenticationServiceException("TOKEN_TYPE_MISMATCH");
            }
            return claims;
        } catch (ExpiredJwtException e) {
            throw new AuthenticationServiceException("TOKEN_EXPIRED", e);
        } catch (JwtException e) {
            throw new AuthenticationServiceException("TOKEN_INVALID", e);
        }
    }

    public Claims parseRefresh(String refreshToken) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getRefreshKey())
                    .clockSkewSeconds(CLOCK_SKEW_SECONDS)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();

            if (!"refresh".equals(claims.get("typ"))) {
                throw new AuthenticationServiceException("TOKEN_TYPE_MISMATCH");
            }
            return claims;
        } catch (ExpiredJwtException e) {
            throw new AuthenticationServiceException("TOKEN_EXPIRED", e);
        } catch (JwtException e) {
            throw new AuthenticationServiceException("TOKEN_INVALID", e);
        }
    }
}
