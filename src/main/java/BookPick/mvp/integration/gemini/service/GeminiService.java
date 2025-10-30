package BookPick.mvp.integration.gemini.service;


import BookPick.mvp.integration.gemini.client.GeminiClient;
import BookPick.mvp.integration.gemini.prompt.SystemInstructionPromptTemplate;
import BookPick.mvp.integration.gemini.prompt.ContentPromptTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final GeminiClient geminiClient;
    private final SystemInstructionPromptTemplate systemPromptTemplate;

    public String generateRecommendation(ContentPromptTemplate contentTemplate) {
        String systemPrompt = systemPromptTemplate.getSystemInstructionPrompt();
        String userPrompt = contentTemplate.toContentPrompt();

        return geminiClient.callGemini(systemPrompt, userPrompt);
    }

    // 결과를 4줄로 파싱
    public String[] parseResult(String result) {
        return result.trim().split("\n");
    }
}
