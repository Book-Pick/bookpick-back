package BookPick.mvp.domain.curation.util.gemini.client;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class GeminiConfig {

    @Value("${api.gemini.api.key}")
    private String apiKey;

    @Value("${api.gemini.api.url}")
    private String apiUrl;
}
