package BookPick.mvp.domain.curation.util.gemini.service;


import BookPick.mvp.domain.curation.model.Curation;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.util.gemini.client.GeminiClient;
import BookPick.mvp.domain.curation.util.gemini.dto.CurationMatchResult;
import BookPick.mvp.domain.curation.util.gemini.prompt.SystemInstructionPromptTemplate;
import BookPick.mvp.domain.curation.util.gemini.prompt.ContentPromptTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final GeminiClient geminiClient;
    private final SystemInstructionPromptTemplate systemPromptTemplate;
    private final CurationRepository curationRepository;

    public String generateRecommendation(ContentPromptTemplate contentTemplate) {
        String systemPrompt = systemPromptTemplate.getSystemInstructionPrompt();
        String userPrompt = contentTemplate.toContentPrompt();

        return geminiClient.callGemini(systemPrompt, userPrompt);
    }

    public String[] parseResult(String result) {
        return result.trim().split("\n");
    }

    @Transactional(readOnly = true)
    public List<CurationMatchResult> recommendCurationsWithMatch(ContentPromptTemplate contentTemplate) {


        // 1. Gemini에게 추천 받기
        String result = generateRecommendation(contentTemplate);
        String[] parsed = parseResult(result);

        // 2. 파싱된 결과 (각 1개씩)
        String recommendedMood = parsed[0].trim();
        String recommendedGenre = parsed[1].trim();
        String recommendedKeyword = parsed[2].trim();
        String recommendedStyle = parsed[3].trim();

        // 3. DB에서 큐레이션 찾기
        List<Curation> curations = curationRepository.findByRecommendation(
                List.of(recommendedMood),
                List.of(recommendedGenre),
                List.of(recommendedKeyword),
                List.of(recommendedStyle)
        );

        // 4. 일치 정보와 함께 반환 (일치 개수 많은 순)
        return curations.stream()
                .map(curation -> CurationMatchResult.of(
                        curation,
                        curation.getUser(),
                        recommendedMood,
                        recommendedGenre,
                        recommendedKeyword,
                        recommendedStyle
                ))
                .sorted((a, b) -> Integer.compare(b.getTotalMatchCount(), a.getTotalMatchCount()))
                .collect(Collectors.toList());
    }
}