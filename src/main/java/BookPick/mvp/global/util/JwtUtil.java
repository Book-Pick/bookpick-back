package BookPick.mvp.global.util;

import BookPick.mvp.domain.user.entity.User;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JwtUtil {


    // 1. 키발급
    static final SecretKey key =
            Keys.hmacShaKeyFor(Decoders.BASE64.decode(
                    "jwtpassword123jwtpassword123jwtpassword123jwtpassword123jwtpassword"
            ));

    // 2. JWT 생성
    public static String createToken(User user) {


        String jwt = Jwts.builder()
                .claim("email", user.getEmail())                             // 이메일
                .claim("role", user.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))                 // 발급 시간
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60))  // 만료 시간
                .signWith(key)                                                  // 비밀키 서명
                .compact();
        return jwt;
    }


    //3. JWT 오픈
    public static Claims extractToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
        return claims;
    }



}