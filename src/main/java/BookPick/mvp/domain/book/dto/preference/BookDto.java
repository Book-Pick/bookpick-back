package BookPick.mvp.domain.book.dto.preference;

import BookPick.mvp.domain.book.entity.Book;

import java.util.List;
import java.util.Set;

public record BookDto(
         String title,
         // Todo 1. author String 단수로 변경 필요
         Set<String> authors,
         String image,
         String isbn
) {
    public BookDto{
        if(authors==null){
            authors=Set.of();
        }
    }
}
