package BookPick.mvp.global.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@Getter
public class JwtConfig {

    @Value("${jwt.access.secret}")
    private static String accessSecret;
    @Value("${jwt.access.expiration}")
    private long accessSecretExp;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;
    @Value("${jwt.refresh.expiration}")
    private long refreshSecretExp;


    SecretKey getSecretKey(String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public SecretKey getAccessSecretKey() {
        return getSecretKey(this.accessSecret);
    }

    public SecretKey getRefreshSecretKey() {
        return getSecretKey(this.refreshSecret);
    }



}
