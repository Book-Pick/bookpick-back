package BookPick.mvp.integration.gemini.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Configuration
@Getter
public class GeminiConfig {

    @Value("${api.gemini.api.key}")
    private String apiKey;

    @Value("${api.gemini.api.url}")
    private String apiUrl;
}