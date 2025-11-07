package BookPick.mvp.domain.curation.util.gemini.prompt;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ContentPromptTemplate {

    private String mbti;
    private String mood;
    private String readingMethod;
    private String genre;
    private String keyword;
    private String readingStyle;

    public String toContentPrompt() {
        return String.format("""
            **User Input**
            MBTI: %s
            Mood: %s
            Reading Method: %s
            Genre: %s
            Keyword: %s
            Reading Style: %s
            """,
            mbti,
            mood,
            readingMethod,
            genre,
            keyword,
            readingStyle
        );
    }
}