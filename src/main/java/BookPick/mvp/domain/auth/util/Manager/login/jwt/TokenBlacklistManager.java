package BookPick.mvp.domain.auth.util.Manager.login.jwt;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklistManager {

    // key: JWT ID (jti), value: 만료 시간
    private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();

    // 블랙리스트 등록
    public void add(String jti, Instant expiration) {
        blacklist.put(jti, expiration);
    }

    // 블랙리스트 체크
    public boolean isBlacklisted(String jti) {
        Instant exp = blacklist.get(jti);
        if (exp == null) return false;

        // 이미 만료된 토큰은 제거
        if (Instant.now().isAfter(exp)) {
            blacklist.remove(jti);
            return false;
        }
        return true;
    }

    // 테스트용: 전체 제거
    public void clear() {
        blacklist.clear();
    }
}
