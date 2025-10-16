package BookPick.mvp.domain.book.dto;


import BookPick.mvp.global.dto.PageInfo;
import java.util.List;

public class BookDtos {

    // -- R --
    public record BookSearchReq(
            String keyword
    ){}
    public record BookSearchRes(
        String title,
        String author,
        String image
) {}
    public record BookSearchPageRes(
        List<BookSearchRes> books,
        PageInfo pageInfo
) {}

}
