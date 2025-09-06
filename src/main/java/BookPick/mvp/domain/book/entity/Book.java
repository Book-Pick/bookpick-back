package BookPick.mvp.domain.book.entity;

import BookPick.mvp.domain.taste.entity.Genre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;



@Entity
@Table(name = "book")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 도서 ID (PK)

    @Column(nullable = false, length = 255)
    private String title; // 도서 제목

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author; // 저자 ID (FK → author 테이블)

    @ManyToOne
    @JoinColumn(name = "translator_id")
    private Translator translator; // 역자 ID (FK → translator 테이블)

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre; // 장르 ID (FK → genre 테이블)

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 시각

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 수정 시각
}
