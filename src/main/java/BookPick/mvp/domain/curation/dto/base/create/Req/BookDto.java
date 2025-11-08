package BookPick.mvp.domain.curation.dto.base.create.Req;

// 책 정보
public record BookDto(
    String title,
    String author,
    String isbn
) {}