package BookPick.mvp.domain.preference.entity;

import BookPick.mvp.domain.preference.dto.ReadingPreference.CreateReq;
import BookPick.mvp.domain.preference.dto.ReadingPreference.UpdateReq;
import BookPick.mvp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReadingPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ 유저와 1:1 관계 (유저 삭제 시 preference도 자동 삭제)
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String mbti;

    @ElementCollection
    @CollectionTable(name = "preference_favorite_authors", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "author")
    private List<String> favoriteAuthors;

    @ElementCollection
    @CollectionTable(name = "preference_favorite_books", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "book")
    private List<String> favoriteBooks;

    @ElementCollection
    @CollectionTable(name = "preference_selection_criteria", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "criteria")
    private List<String> selectionCriteria;

    @ElementCollection
    @CollectionTable(name = "preference_reading_habits", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "habit")
    private List<String> readingHabits;

    @ElementCollection
    @CollectionTable(name = "preference_genres", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "genre")
    private List<String> preferredGenres;

    @ElementCollection
    @CollectionTable(name = "preference_keywords", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "keyword")
    private List<String> keywords;

    @ElementCollection
    @CollectionTable(name = "preference_trends", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "trend")
    private List<String> recommendedTrends;

    // ---- 팩토리 메서드 ----
    public static ReadingPreference from(CreateReq req, User user) {
        ReadingPreference pref = new ReadingPreference();
        pref.user = user;
        pref.mbti = req.mbti();
        pref.favoriteAuthors = req.favoriteAuthors();
        pref.favoriteBooks = req.favoriteBooks();
        pref.selectionCriteria = req.selectionCriteria();
        pref.readingHabits = req.readingHabits();
        pref.preferredGenres = req.preferredGenres();
        pref.keywords = req.keywords();
        pref.recommendedTrends = req.recommendedTrends();
        return pref;
    }

    public void apply(UpdateReq req) {
        this.mbti = req.mbti();
        this.favoriteAuthors = req.favoriteAuthors();
        this.favoriteBooks = req.favoriteBooks();
        this.selectionCriteria = req.selectionCriteria();
        this.readingHabits = req.readingHabits();
        this.preferredGenres = req.preferredGenres();
        this.keywords = req.keywords();
        this.recommendedTrends = req.recommendedTrends();
    }
}
