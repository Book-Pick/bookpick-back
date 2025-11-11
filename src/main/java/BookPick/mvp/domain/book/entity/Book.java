package BookPick.mvp.domain.book.entity;

import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.book.dto.preference.BookDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToMany
    @JoinTable(
            name = "book_id",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();      // 하나의 책마다 여러 작가들 존재

    private String image;

    private String isbn;


    public Book() {

    }

    public static Book from(BookDto bookDto) {

        List<String> authors

        return Book.builder()
                .title(bookDto.title())
                .authors(new HashSet<>(bookDto.authors()))
                .image(bookDto.image())
                .isbn(bookDto.isbn())
                .build();
    }
}
