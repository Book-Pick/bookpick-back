package BookPick.mvp.domain.Book.entity;


import BookPick.mvp.domain.Author.entity.Author;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;  // 책:작가 = N:1

    @Column(length = 200)
    private String description;

    @Column(length = 50)
    private String genre;
}

