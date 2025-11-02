package BookPick.mvp;


import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.integration.gemini.dto.CurationMatchResult;
import BookPick.mvp.integration.gemini.prompt.ContentPromptTemplate;
import BookPick.mvp.integration.gemini.service.GeminiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class GeminiClientMain {

    public static void main(String[] args) {
        SpringApplication.run(GeminiClientMain.class, args);
    }

    @Bean
    CommandLineRunner test(GeminiService geminiService) {
        return args -> {

            ContentPromptTemplate template = ContentPromptTemplate.builder()
                    .mbti("INFP")
                    .mood("새벽 시간, 카페, 혼자만의 시간")
                    .readingMethod("한 번에 완독하는 편, 조용한 곳에서만 읽는 편")
                    .genre("에세이, 철학, 소설")
                    .keyword("성장, 공감, 현실")
                    .readingStyle("몰입형, 감성적, 깊이 있는 사색")
                    .build();

            System.out.println("=== 추천된 큐레이션 ===");
            List<CurationMatchResult> results = geminiService.recommendCurationsWithMatch(template);

            System.out.println("총 " + results.size() + "개의 큐레이션 발견\n");

            results.forEach(result -> {
                Curation c = result.getCuration();
                System.out.println("📚 책 제목: " + c.getBookTitle());
                System.out.println("   저자: " + c.getBookAuthor());
                System.out.println("   총 일치: " + result.getTotalMatchCount() + "개");
                System.out.println("\n=== 일치한 태그 ===");
                System.out.print(result.getMatchedString());  // ← 일치하는 것만 출력!
                System.out.println("   인기도: " + c.getPopularityScore());
                System.out.println("-----------------------------------");
            });
        };
    }
}