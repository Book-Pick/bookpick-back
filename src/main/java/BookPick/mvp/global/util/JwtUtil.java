package BookPick.mvp.global.util;

import BookPick.mvp.domain.auth.exception.InvalidTokenTypeException;
import BookPick.mvp.domain.auth.exception.JwtTokenExpiredException;
import BookPick.mvp.domain.auth.service.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


@Component
public class JwtUtil {
    // 1. 키발급
    final SecretKey key =
            Keys.hmacShaKeyFor(Decoders.BASE64.decode(
                    "jwtpassword123jwtpassword123jwtpassword123jwtpassword123jwtpassword"
            ));

    // (추가) 토큰 수명 상수
    private static final long ACCESS_TTL_MS = 1000L * 60 * 60;          // 1시간
    private static final long REFRESH_TTL_MS = 1000L * 60 * 60 * 24 * 14; // 14일

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
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // expiration : 만료
                .signWith(key)
                .compact();
        return jwt;
    }

    // ✅ 2-1. Refresh 토큰 생성 (여기 추가)
    public String createRefreshToken(Authentication auth) {
        CustomUserDetails usr = (CustomUserDetails) auth.getPrincipal();

        // refresh 토큰에는 최소 정보만: subject/email + typ 정도만 권장
        return Jwts.builder()
                .claim("email", usr.getUsername())
                .claim("typ", "refresh") // (권장) 토큰 타입 명시
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TTL_MS))
                .signWith(key)
                .compact();
    }


    //3. JWT 오픈
    public Claims extractToken(String token) {

        try {
            Claims claims = Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(token).getPayload();
            return claims;
        } catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenTypeException();
        }
    }
}



