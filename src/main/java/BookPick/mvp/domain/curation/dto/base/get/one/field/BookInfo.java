package BookPick.mvp.domain.curation.dto.base.get.one.field;

import BookPick.mvp.domain.curation.entity.Curation;

public record BookInfo(String title, String author, String isbn, String imageUrl)

{
    public static BookInfo of(Curation curation){
        return new BookInfo(curation.getBookTitle(), curation.getBookAuthor(), curation.getBookIsbn(), curation.getBookImageUrl());
    }
}
