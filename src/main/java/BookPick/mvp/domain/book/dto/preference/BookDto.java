package BookPick.mvp.domain.book.dto.preference;

import BookPick.mvp.domain.book.entity.Book;

import java.util.List;
import java.util.Set;

public record BookDto(
         String title,
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
