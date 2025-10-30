package BookPick.mvp;



import BookPick.mvp.integration.gemini.client.GeminiClient;
import BookPick.mvp.integration.gemini.prompt.ContentPromptTemplate;
import BookPick.mvp.integration.gemini.prompt.SystemInstructionPromptTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;





@SpringBootApplication
public class GeminiClientMain {

    public static void main(String[] args) {
        SpringApplication.run(GeminiClientMain.class, args);
    }

    @Bean
    CommandLineRunner test(GeminiClient client, SystemInstructionPromptTemplate systemTemplate) {
        return args -> {

            // ContentPromptTemplate 생성
            ContentPromptTemplate contentTemplate = ContentPromptTemplate.builder()
                .mbti("INFP")
                .mood("새벽 시간, 카페, 혼자만의 시간")
                .readingMethod("한 번에 완독하는 편, 조용한 곳에서만 읽는 편")
                .genre("에세이, 철학, 소설")
                .keyword("성장, 공감, 현실")
                .readingStyle("몰입형, 감성적, 깊이 있는 사색")
                .build();

            // 시스템 프롬프트와 콘텐츠 프롬프트
            String systemPrompt = systemTemplate.getSystemInstructionPrompt();
            String userPrompt = contentTemplate.toContentPrompt();

            System.out.println("=== 추천 결과 ===");
            String result = client.callGemini(systemPrompt, userPrompt);
            System.out.println(result);
        };
    }
}