package BookPick.mvp.domain.book.Controller;

import BookPick.mvp.domain.auth.service.CustomUserDetails;
import BookPick.mvp.domain.book.dto.search.BookSearchPageRes;
import BookPick.mvp.domain.book.dto.search.BookSearchReq;
import BookPick.mvp.domain.book.util.kakaoApi.BookSearchService;
import BookPick.mvp.domain.user.util.CurrentUserCheck;
import BookPick.mvp.global.api.ApiResponse;
import BookPick.mvp.global.api.SuccessCode.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookSearchController {

    private final BookSearchService bookSearchService;
    private final CurrentUserCheck currentUserCheck;

    @Operation(
            summary = "책 검색",
            description = "검색어로 책 목록 조회",
            tags = {"Book Search"})
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "책 검색 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요합니다", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<BookSearchPageRes>> searchBookList(
            @RequestBody BookSearchReq req,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        BookSearchPageRes res = bookSearchService.getBookSearchList(req);
        currentUserCheck.validateLoginUser(currentUser);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessCode.BOOK_LIST_READ_SUCCESS, res));
    }
}
