package BookPick.mvp.domain.book.service;

import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.author.repository.AuthorRepository;
import BookPick.mvp.domain.book.entity.Book;
import BookPick.mvp.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookSaveService {
    private final BookRepository bookRepository;

    // 1.  독서취향에서 책 꺼내서, 첵 db에 책 등록되어있지 않으면 저장
    public void saveIfNotExists(List<Book> books) {
        List<Book> preferredBooks = books;
        for (Book preferedBook : preferredBooks) {
            Optional<Book> existingBook = bookRepository.findByTitle(preferedBook.getTitle());
            if (existingBook.isEmpty()) {
                bookRepository.save(preferedBook);
            }
        }

    }


}
