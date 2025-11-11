package BookPick.mvp.domain.ReadingPreference.entity;

import BookPick.mvp.domain.ReadingPreference.dto.Update.ReadingPreferenceUpdateReq;
import BookPick.mvp.domain.book.entity.Book;
import BookPick.mvp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user_reading_preference")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String mbti;

    @ElementCollection
    @CollectionTable(name = "preference_authors", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "authors")
    private List<String> favoriteAuthors;


    @ManyToMany
    @JoinTable(
            name = "preference_favorite_books",
            joinColumns = @JoinColumn(name = "preference_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> favoriteBooks;

    @ElementCollection
    @CollectionTable(name = "preference_moods", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "mood")
    private List<String> moods;

    @ElementCollection
    @CollectionTable(name = "preference_styles", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "habit")
    private List<String> readingHabits;

    @ElementCollection
    @CollectionTable(name = "preference_genres", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "genre")
    private List<String> genres;

    @ElementCollection
    @CollectionTable(name = "preference_keywords", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "keyword")
    private List<String> keywords;

    @ElementCollection
    @CollectionTable(name = "preference_trends", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "trend")
    private List<String> trends;

    public void update(ReadingPreferenceUpdateReq req) {
        if (req.mbti() != null) this.mbti = req.mbti();
        if (req.favoriteAuthors() != null) this.favoriteAuthors = req.favoriteAuthors();
        if (req.favoriteBooks() != null) this.favoriteBooks = req.favoriteBooks();
        if (req.moods() != null) this.moods = req.moods();
        if (req.readingHabits() != null) this.readingHabits = req.readingHabits();
        if (req.genres() != null) this.genres = req.genres();
        if (req.keywords() != null) this.keywords = req.keywords();
        if (req.trends() != null) this.trends = req.trends();
    }

    }


