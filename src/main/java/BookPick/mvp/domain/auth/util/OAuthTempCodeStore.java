package BookPick.mvp.domain.auth.util;

import BookPick.mvp.domain.auth.dto.LoginRes;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OAuthTempCodeStore {

    private static final long TTL_SECONDS = 30;

    private final Map<String, TempCodeData> store = new ConcurrentHashMap<>();

    public String store(LoginRes loginRes) {
        String code = UUID.randomUUID().toString();
        store.put(code, new TempCodeData(loginRes, Instant.now()));
        return code;
    }

    public Optional<LoginRes> consume(String code) {
        TempCodeData data = store.remove(code);
        if (data == null) {
            return Optional.empty();
        }
        if (data.isExpired()) {
            return Optional.empty();
        }
        return Optional.of(data.loginRes());
    }

    @Scheduled(fixedRate = 300000) // 5분마다 실행
    public void cleanupExpiredCodes() {
        Instant now = Instant.now();
        store.entrySet().removeIf(entry -> entry.getValue().isExpiredAt(now));
    }

    private record TempCodeData(LoginRes loginRes, Instant createdAt) {
        boolean isExpired() {
            return isExpiredAt(Instant.now());
        }

        boolean isExpiredAt(Instant now) {
            return createdAt.plusSeconds(TTL_SECONDS).isBefore(now);
        }
    }
}
