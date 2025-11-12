package BookPick.mvp.domain.curation.service.list;

import BookPick.mvp.domain.curation.dto.prefer.ReadingPreferenceInfo;
import BookPick.mvp.domain.curation.util.gemini.dto.CurationMatchResult;
import BookPick.mvp.domain.curation.util.gemini.prompt.ContentPromptTemplate;
import BookPick.mvp.domain.curation.util.gemini.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurationRecommendationService {

    private final GeminiService geminiService;

    public List<CurationMatchResult> recommend(ReadingPreferenceInfo preferenceInfo) {

        return geminiService.recommendCurationsWithMatch(


                // 1. 제미나이 프롬프트 생성
                ContentPromptTemplate.builder()
                        .mbti(preferenceInfo.mbti())
                        .mood(String.join(", ", preferenceInfo.moods()))        //, 으로 하나의 문자열로 변경 -> 제미나이 프롬프트에 넣기 위해서
                        .readingMethod(String.join(", ", preferenceInfo.readingHabits()))
                        .genre(String.join(", ", preferenceInfo.genres()))
                        .keyword(String.join(", ", preferenceInfo.keywords()))
                        .readingStyle(String.join(", ", preferenceInfo.readingStyles()))
                        .build()
        );
    }
}
