package BookPick.mvp.domain.book.dto.search;

// -- R --
public record BookSearchReq(
        String keyword,
        Integer page
) {
}
