package BookPick.mvp.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("gemini-recommendations");

        // TTL 설정: 10분 후 자동 삭제 + 최대 1000개 캐시
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(10, TimeUnit.MINUTES) // 10분 후 자동 갱신
                        .maximumSize(1000) // 메모리 보호
                        .recordStats()); // 캐시 통계 (선택)

        return cacheManager;
    }
}
