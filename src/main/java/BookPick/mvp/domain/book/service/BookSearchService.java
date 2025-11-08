package BookPick.mvp.domain.book.service;

import BookPick.mvp.domain.book.dto.BookDtos.*;
import BookPick.mvp.global.dto.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BookSearchService {

    @Value("${api.kakao.key}")
    private String kakaoApiKey;

    private static final String API_URL = "https://dapi.kakao.com/v3/search/book";

    public BookSearchPageRes getBookSearchList(BookSearchReq req) {

        RestTemplate restTemplate = new RestTemplate();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        // 요청 URL 구성
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("query", req.keyword())
                .queryParam("page", req.page())
                .queryParam("size", 10)
                .build();


        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // 카카오 API 호출
        ResponseEntity<Map> response = restTemplate.exchange(
                uri.toUriString(),
                HttpMethod.GET,
                requestEntity,
                Map.class
        );

        // documents 배열 추출
        List<Map<String, Object>> documents = (List<Map<String, Object>>) response.getBody().get("documents");

        // 필요한 데이터만 변환
        List<BookSearchRes> books = new ArrayList<>();
        for (Map<String, Object> doc : documents) {
            String title = (String) doc.get("title");
            List<String> authors = (List<String>) doc.get("authors");
            String author = authors != null && !authors.isEmpty() ? authors.get(0) : "저자 미상";
            String image = (String) doc.get("thumbnail");

            books.add(new BookSearchRes(title, author, image));
        }

        // meta 데이터 추출 → PageInfo 변환
        Map<String, Object> meta = (Map<String, Object>) response.getBody().get("meta");
        int totalCount = ((Number) meta.get("total_count")).intValue();
        boolean isEnd = (boolean) meta.get("is_end");

        // PageInfo 매핑 (현재 페이지는 Kakao API 요청 기준)
        PageInfo pageInfo = new PageInfo(
                req.page(),                                  // currentPage (요청 page)
                (int) Math.ceil((double) totalCount / 10), // totalPages (총 페이지 수)
                totalCount,                          // totalElements (총 아이템 수)
                !isEnd                               // hasNext (다음 페이지 여부)
        );

        // 최종 응답 DTO 반환
        return new BookSearchPageRes(books, pageInfo);
    }
}
