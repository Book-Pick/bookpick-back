package BookPick.mvp.domain.book.Controller;

import BookPick.mvp.domain.book.dto.BookDtos.*;
import BookPick.mvp.domain.book.service.BookSearchService;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookSearchController {

    private final BookSearchService bookSearchService;

    @Operation(summary = "책 검색", description = "검색어로 책 목록 조회", tags = {"Book Search"})
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<BookSearchPageRes>> searchBookList(@RequestBody BookSearchReq req){
        BookSearchPageRes res = bookSearchService.getBookSearchList(req);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.BOOK_LIST_READ_SUCCESS, res));
    }
}
