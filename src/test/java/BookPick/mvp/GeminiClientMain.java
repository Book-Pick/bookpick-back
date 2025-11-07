package BookPick.mvp;

import BookPick.mvp.domain.curation.model.Curation;
import BookPick.mvp.domain.curation.util.gemini.dto.CurationMatchResult;
import BookPick.mvp.domain.curation.util.gemini.prompt.ContentPromptTemplate;
import BookPick.mvp.domain.curation.util.gemini.service.GeminiService;
import BookPick.mvp.domain.preference.entity.ReadingPreference;
import BookPick.mvp.domain.user.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SpringBootApplication
public class GeminiClientMain {

    public static void main(String[] args) {
        SpringApplication.run(GeminiClientMain.class, args);
    }

    @Autowired
    private GeminiService geminiService;

    @Bean
    CommandLineRunner runTest() {
        return args -> {
            User user = new User(); // 테스트용 유저 생성
            ReadingPreference preference = ReadingPreference.builder()
                    .user(user)
                    .mbti("INFP")
                    .moods(List.of("새벽 시간, 카페, 혼자만의 시간"))
                    .readingHabits(List.of("한 번에 완독하는 편, 조용한 곳에서만 읽는 편"))
                    .genres(List.of("에세이, 철학, 소설"))
                    .keywords(List.of("성장, 공감, 현실"))
                    .trends(List.of("몰입형, 감성적, 깊이 있는 사색"))
                    .build();

            ContentPromptTemplate template = ContentPromptTemplate.builder()
                    .mbti(preference.getMbti())
                    .mood(String.join(", ", preference.getMoods()))
                    .readingMethod(String.join(", ", preference.getReadingHabits()))
                    .genre(String.join(", ", preference.getGenres()))
                    .keyword(String.join(", ", preference.getKeywords()))
                    .readingStyle(String.join(", ", preference.getTrends()))
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
                System.out.print(result.getMatchedString());
                System.out.println("   인기도: " + c.getPopularityScore());
                System.out.println("-----------------------------------");
            });
        };
    }
}
