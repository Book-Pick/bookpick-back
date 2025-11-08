package BookPick.mvp.domain.curation.util.gemini.prompt;

import org.springframework.stereotype.Component;

@Component
public class SystemInstructionPromptTemplate {


    // 1. 유저의 독서 취향을 가지고
    // 2. 하나씩 뽑아라 아래 항목들중에서
    // 3. 그리고 해당 키워드들을 가지고 큐레이션을 찾아서 유저한테 소개할 것이다.
    private static final String SYSTEM_PROMPT = """
            Based on the user's reading preferences, select exactly one item from each of Mood, Genre, Keyword, and ReadingStyle. 
            You must only choose from the provided lists. 
            Output only the selected values, one per line, without any labels or formatting.
            
            Mood list: 퇴근 후, 따뜻한 차 한잔, 비 오는 날, 눈 오는 날, 지하철·버스, 카페, 침대에서, 공원, 도서관, 서점에서, 새벽 시간, 주말 오후, 점심시간, 늦은 밤, 잠들기 전, 혼자만의 시간, 창가에서, 음악과 함께, 여행 중, 휴가 중
            Genre list: 소설, 에세이, 역사, 예술, 자기개발, 경제, 심리학, 사회, 교육, 과학, 철학, 종교
            Keyword list: 위로, 성장, 사랑, 공감, 지식, 유머, 추리, 모험, 판타지, 현실, 미래, 과거
            Reading Style list: 속독형, 몰입형, 정독형, 취향 탐색형, 스토리 중심, 지식 위주, 감성적, 논리적, 창의적, 실용적, 비평적, 상상력 중시, 느긋한 독서, 깊이 있는 사색, 가볍게 즐기기
            """;

    public String getSystemInstructionPrompt() {
        return SYSTEM_PROMPT;
    }
}
