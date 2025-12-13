package BookPick.mvp.domain.curation.entity;

import BookPick.mvp.domain.curation.dto.base.CurationReq;
import BookPick.mvp.domain.curation.dto.base.update.CurationUpdateReq;
import BookPick.mvp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "curation")
@AllArgsConstructor
public class Curation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;


    // Todo 2. 추후 썸네일 클래스로 변경 필요
    private String thumbnailUrl;
    private String thumbnailColor;


    // Todo 1. 추후 Book 클래스로 변경 필요
    @Column(nullable = false)
    private String bookTitle;
    private String bookAuthor;
    private String bookIsbn;
    private String bookImageUrl;


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


    @Builder.Default
    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Builder.Default
    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Builder.Default
    @Column(name = "comment_count")
    private Integer commentCount = 0;


    @Builder.Default
    @Column(name = "popularity_score")
    private Integer popularityScore = 0;

    @Column(name = "is_draft")
    private boolean isDrafted = false;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    //Todo 1. 소프트 델리트 구현 필요

    public Curation() {

    }


    public void curationUpdate(CurationUpdateReq req) {
        this.title = req.title();
        this.thumbnailUrl = req.thumbnail().imageUrl();
        this.thumbnailColor = req.thumbnail().imageColor();
        this.bookTitle = req.book().title();
        this.bookAuthor = req.book().author();
        this.bookIsbn = req.book().isbn();
        this.bookImageUrl = req.book().imageUrl();
        this.review = req.review();
        this.moods = req.recommend().moods();
        this.genres = req.recommend().genres();
        this.keywords = req.recommend().keywords();
        this.styles = req.recommend().styles();
    }

    public static Curation createDraft(User user, CurationReq req) {
        Curation curation = Curation.from(user, req);
        curation.setDrafted(true);

        return curation;
    }


    public void increaseViewCount() {
        this.viewCount++;
        updatePopularityScore(); // 인기도 재계산
    }

    public void updatePopularityScore() {
        this.popularityScore = (likeCount * 3) + (commentCount * 2) + (viewCount * 1);
    }


    // 팩토리 메서드
    public static Curation from(User user, CurationReq req) {
        return Curation.builder()
                .user(user)
                .title(req.title())
                .thumbnailUrl(req.thumbnail().imageUrl())
                .thumbnailColor(req.thumbnail().imageColor())
                .bookTitle(req.book().title())
                .bookAuthor(req.book().author())
                .bookIsbn(req.book().isbn())
                .review(req.review())
                .moods(req.recommend().moods())
                .genres(req.recommend().genres())
                .keywords(req.recommend().keywords())
                .styles(req.recommend().styles())
                .build();
    }

}