package BookPick.mvp.domain.book.dto.preference;

import BookPick.mvp.domain.book.entity.Book;

import java.util.List;
import java.util.Set;

public record BookDto(
         String title,
         String author,
         String image,
         String isbn
) {

}
