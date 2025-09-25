package BookPick.mvp.domain.preference.entity;

import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.book.entity.Book;
import BookPick.mvp.domain.preference.dto.PreferenceDtos.CreateReq;
import BookPick.mvp.domain.preference.dto.PreferenceDtos.UpdateReq;
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
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저와 1:1 관계 (유저당 하나만)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", unique = true, nullable = false)
    private User user;

    private String mbti;

    @ManyToMany
    @JoinTable(
        name = "preference_authors",
        joinColumns = @JoinColumn(name = "preference_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> favoriteAuthors;


    @ManyToMany
    @JoinTable(
        name = "preference_books",
        joinColumns = @JoinColumn(name = "preference_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> favoriteBooks;

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
    public static UserPreference from(CreateReq req, User user) {
        UserPreference pref = new UserPreference();
        pref.user = user;
        pref.mbti = req.mbti();
        // Author/Book 매핑은 별도 Repository에서 가져와야 함
        pref.selectionCriteria = req.selectionCriteria();
        pref.readingHabits = req.readingHabits();
        pref.preferredGenres = req.preferredGenres();
        pref.keywords = req.keywords();
        pref.recommendedTrends = req.recommendedTrends();
        return pref;
    }

    // ---- 수정 적용 ----
    public void apply(UpdateReq req) {
        this.mbti = req.mbti();
        this.selectionCriteria = req.selectionCriteria();
        this.readingHabits = req.readingHabits();
        this.preferredGenres = req.preferredGenres();
        this.keywords = req.keywords();
        this.recommendedTrends = req.recommendedTrends();
    }

}
