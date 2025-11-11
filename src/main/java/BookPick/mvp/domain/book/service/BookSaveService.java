package BookPick.mvp.domain.book.service;

import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.author.repository.AuthorRepository;
import BookPick.mvp.domain.author.service.AuthorSaveService;
import BookPick.mvp.domain.book.dto.preference.BookDto;
import BookPick.mvp.domain.book.entity.Book;
import BookPick.mvp.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookSaveService {

    private final BookRepository bookRepository;
    private final AuthorSaveService authorSaveService;

    // 1. Book 리스트
    public void saveBookIfNotExists(Set<Book> books) {

        for (Book book : books) {
            saveBookIfNotExists(book); // 단건 재사용
        }
    }

    // 2. Book 단건
    public void saveBookIfNotExists(Book book) {
        bookRepository.findByTitle(book.getTitle())
                .orElseGet(() -> {
                    authorSaveService.saveAuthorIfNotExists(book.getAuthors()); // 작가 먼저 저장
                    return bookRepository.save(book);
                });
    }

    // 3. BookDto 리스트
    public Set<Book> saveBookIfNotExistsDto(Set<BookDto> bookDtos) {
        Set<Book> books= new HashSet<>();
        for (BookDto dto : bookDtos) {
            books.add(saveBookIfNotExistsDto(dto));
        }

        return books;
    }

    // 4. BookDto 단건
    public Book saveBookIfNotExistsDto(BookDto dto) {

        return bookRepository.findByTitle(dto.title())
                .orElseGet(() -> {      // 책이 없으면
                    // authors 저장
                    Set<Author> authors = authorSaveService.saveAuthorsIfNotExistsByName(dto.authors());
                    // Book 객체 변환 후 저장
                    Book book = Book.from(dto, authors);
                    return bookRepository.save(book);
                });
    }
}


