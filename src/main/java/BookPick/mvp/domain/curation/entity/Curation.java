package BookPick.mvp.domain.curation.entity;

import BookPick.mvp.domain.curation.dto.update.CurationUpdateReq;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "curation")
public class Curation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private String thumbnailUrl;
    private String thumbnailColor;

    @Column(nullable = false)
    private String bookTitle;
    private String bookAuthor;
    private String bookIsbn;

    @Column(columnDefinition = "TEXT")
    private String review;

    // 매핑 테이블로 변경!
    @ElementCollection
    @CollectionTable(name = "curation_moods", joinColumns = @JoinColumn(name = "curation_id"))
    @Column(name = "mood")
    private List<String> moods;

    @ElementCollection
    @CollectionTable(name = "curation_genres", joinColumns = @JoinColumn(name = "curation_id"))
    @Column(name = "genre")
    private List<String> genres;

    @ElementCollection
    @CollectionTable(name = "curation_keywords", joinColumns = @JoinColumn(name = "curation_id"))
    @Column(name = "keyword")
    private List<String> keywords;

    @ElementCollection
    @CollectionTable(name = "curation_styles", joinColumns = @JoinColumn(name = "curation_id"))
    @Column(name = "style")
    private List<String> styles;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Builder
    public Curation(Long userId, String thumbnailUrl, String thumbnailColor,
                    String bookTitle, String bookAuthor, String bookIsbn,
                    String review, List<String> moods, List<String> genres,
                    List<String> keywords, List<String> styles) {
        this.userId = userId;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailColor = thumbnailColor;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookIsbn = bookIsbn;
        this.review = review;
        this.moods = moods;
        this.genres = genres;
        this.keywords = keywords;
        this.styles = styles;
    }

    public void update(CurationUpdateReq req) {
        this.thumbnailUrl = req.thumbnail().imageUrl();
        this.thumbnailColor = req.thumbnail().imageColor();
        this.bookTitle = req.book().title();
        this.bookAuthor = req.book().author();
        this.bookIsbn = req.book().isbn();
        this.review = req.review();
        this.moods = req.recommend().moods();
        this.genres = req.recommend().genres();
        this.keywords = req.recommend().keywords();
        this.styles = req.recommend().styles();
    }
}