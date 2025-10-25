package BookPick.mvp.domain.curation.entity;

import BookPick.mvp.domain.curation.dto.update.CurationUpdateReq;
import BookPick.mvp.domain.user.entity.User;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "comment_count")
    private Integer commentCount = 0;

    @Column(name = "popularity_score")
    private Integer popularityScore = 0;


    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;


    @Builder
    public Curation(User user, String thumbnailUrl, String thumbnailColor,
                    String bookTitle, String bookAuthor, String bookIsbn,
                    String review, List<String> moods, List<String> genres,
                    List<String> keywords, List<String> styles) {
        this.user = user;
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

    public void increaseViewCount() {
        this.viewCount++;
        updatePopularityScore(); // 인기도 재계산
    }
    public void updatePopularityScore() {
        this.popularityScore = (likeCount * 3) + (viewCount * 2) + (commentCount * 1);
    }
}