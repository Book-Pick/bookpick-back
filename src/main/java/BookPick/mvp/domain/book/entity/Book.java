package BookPick.mvp.domain.book.entity;

import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.book.dto.preference.BookDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @JsonIgnore // Todo 1. 큐레이션에 작가의 대한 정보가 필요 없기 때문에 null -> 추후 디벨렆 예정
    private Author author;     // 하나의 책마다 여러 작가들 존재

    private String image;

    private String isbn;

    public Book() {

    }


    public static Book from(BookDto bookDto, Author author) {


        return Book.builder()
                .title(bookDto.title())
                .author(author)
                .image(bookDto.image())
                .isbn(bookDto.isbn())
                .build();
    }
}
