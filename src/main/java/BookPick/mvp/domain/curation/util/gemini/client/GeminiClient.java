package BookPick.mvp.domain.curation.util.gemini.client;



import BookPick.mvp.domain.curation.util.gemini.GeminiErrorException;
import BookPick.mvp.domain.curation.util.gemini.enums.GeminiErrorCode;
import BookPick.mvp.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private static final int MAX_REQUESTS_PER_MINUTE = 30;

    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final AtomicLong windowStart = new AtomicLong(System.currentTimeMillis());

    private final GeminiConfig config;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String callGemini(String systemInstruction, String userContent) {
        checkRateLimit();
        try {
            String url = config.getApiUrl() + "?key=" + config.getApiKey();

            // 요청 바디 생성 (올바른 구조)
            String requestBody = String.format("""
                            {
                                "system_instruction": {
                                    "parts": [
                                        {"text": "%s"}
                                    ]
                                },
                                "contents": [
                                    {
                                        "parts": [
                                            {"text": "%s"}
                                        ]
                                    }
                                ]
                            }
                            """,
                    systemInstruction.replace("\"", "\\\"").replace("\n", "\\n"),
                    userContent.replace("\"", "\\\"").replace("\n", "\\n")
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // 응답에서 텍스트 추출
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

        } catch (Exception e) {
            throw new GeminiErrorException();
        }
    }

    private void checkRateLimit() {
        long now = System.currentTimeMillis();
        long windowStartTime = windowStart.get();

        // 1분 경과 시 카운터 리셋
        if (now - windowStartTime >= 60_000) {
            requestCount.set(0);
            windowStart.set(now);
        }

        if (requestCount.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
            throw new BusinessException(GeminiErrorCode.GEMINI_API_RATE_LIMITED);
        }
    }
}