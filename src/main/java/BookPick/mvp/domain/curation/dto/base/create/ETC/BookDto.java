package BookPick.mvp.domain.curation.dto.base.create.ETC;

import BookPick.mvp.domain.curation.dto.base.get.one.BookInfo;
import BookPick.mvp.domain.curation.entity.Curation;

// 책 정보
public record BookDto(
    String title,
    String author,
    String isbn,
    String imageUrl
) {
  public static BookDto of(Curation curation){
        return new BookDto(curation.getBookTitle(), curation.getBookAuthor(), curation.getBookIsbn(), curation.getBookImageUrl());
    }

}