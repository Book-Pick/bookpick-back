package BookPick.mvp.global.util;

import BookPick.mvp.domain.auth.exception.InvalidTokenTypeException;
import BookPick.mvp.domain.auth.exception.JwtTokenExpiredException;
import BookPick.mvp.domain.auth.service.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Component
public class JwtUtil {
    // 1. 키발급

    private final SecretKey accessKey;
    private final SecretKey refreshKey;
    private final long accessTtl;
    private final long refreshTtl;

    // 생성자 주입
    public JwtUtil(
            @Value("${jwt.access.secret}") String accessSecret,
            @Value("${jwt.refresh.secret}") String refreshSecret,
            @Value("${jwt.access.expiration}") long accessTtl,
            @Value("${jwt.refresh.expiration}") long refreshTtl
    ) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));
        this.accessTtl = accessTtl;
        this.refreshTtl = refreshTtl;
    }

    // 2. JWT 생성
    public String createAccessToken(Authentication auth) {
        CustomUserDetails usr = (CustomUserDetails) auth.getPrincipal();

        String authorities = auth.getAuthorities().stream()                 //getAuthorities -> List<auth객체> return
                .map(a -> a.getAuthority())   // getAuthority() -> String return
                .collect(Collectors.joining(","));


        String jwt = Jwts.builder()
                .claim("userId", usr.getId())
                .claim("email", usr.getUsername())
                .claim("authorities", authorities)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + this.accessTtl))
                .signWith(accessKey)
                .compact();
        return jwt;
    }

    // ✅ 2-1. Refresh 토큰 생성 (여기 추가)
    public String createRefreshToken(Authentication auth) {
        CustomUserDetails usr = (CustomUserDetails) auth.getPrincipal();

        // refresh 토큰에는 최소 정보만: subject/email + typ 정도만 권장
        return Jwts.builder()
                .claim("userId", usr.getId())  // 여기 추가
                .claim("email", usr.getUsername())
                .claim("typ", "refresh") // (권장) 토큰 타입 명시
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + this.refreshTtl))
                .signWith(refreshKey)
                .compact();
    }


    //3. JWT 오픈
    public Claims extractToken(String token) {

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(accessKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims;


        } catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenTypeException();
        }
    }

    // Access Token 파싱
    public Claims extractAccessToken(String token) {
        return extractToken(token, accessKey);
    }

    // Refresh Token 파싱
    public Claims extractRefreshToken(String token) {
        return extractToken(token, refreshKey);
    }

    // 공통 파싱 로직
    public static Claims extractToken(String token, SecretKey key) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims;

        } catch (ExpiredJwtException e) {
            // 토큰 만료 예외를 커스텀 예외로 던짐
            throw new JwtTokenExpiredException();
        } catch (JwtException | IllegalArgumentException e) {
            // 잘못된 토큰 예외를 커스텀 예외로
            log.warn("JWT 파싱 실패: {}", e.getClass().getSimpleName());
            throw new InvalidTokenTypeException();
        }

    }

    // 토큰 유효성 검증 (Access / Refresh 구분)
    public boolean validateToken(String token, boolean isAccessToken) {
        try {
            if (isAccessToken) {
                extractAccessToken(token);
            } else {
                extractRefreshToken(token);
            }
            return true; // 예외가 안 나면 유효
        } catch (JwtTokenExpiredException e) {
            log.debug("토큰 만료");
            return false;
        } catch (InvalidTokenTypeException | JwtException e) {
            log.debug("유효하지 않은 토큰");
            return false;
        }
    }
}



