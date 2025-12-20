package BookPick.mvp.domain.ReadingPreference.entity;

import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceReq;
import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.author.service.AuthorSaveService;
import BookPick.mvp.domain.book.dto.preference.BookDto;
import BookPick.mvp.domain.book.entity.Book;
import BookPick.mvp.domain.book.service.BookSaveService;
import BookPick.mvp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "reading_preference")
@Getter
@Setter
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

    @ManyToMany
    @JoinTable(
            name = "preference_books",
            joinColumns = @JoinColumn(name = "preference_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> favoriteBooks;

    @ManyToMany
    @JoinTable(
            name = "preference_authors",
            joinColumns = @JoinColumn(name = "preference_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> favoriteAuthors;


    @ElementCollection
    @CollectionTable(name = "preference_moods", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "mood")
    private List<String> moods;

    @ElementCollection
    @CollectionTable(name = "preference_readinghabits", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "reading_habits")
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
    @CollectionTable(name = "preference_readingstyles", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "reading_style")
    private List<String> readingStyles;

    private boolean isCompleted = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public void update(ReadingPreferenceReq req) {
        if (req.mbti() != null) this.mbti = req.mbti();

        if (req.moods() != null) {
            this.moods.clear();
            this.moods.addAll(req.moods());
        }

        if (req.readingHabits() != null) {
            this.readingHabits.clear();
            this.readingHabits.addAll(req.readingHabits());
        }

        if (req.genres() != null) {
            this.genres.clear();
            this.genres.addAll(req.genres());
        }

        if (req.keywords() != null) {
            this.keywords.clear();
            this.keywords.addAll(req.keywords());
        }

        if (req.readingStyles() != null) {
            this.readingStyles.clear();
            this.readingStyles.addAll(req.readingStyles());
        }
    }



    public static ReadingPreference clearPreferences(User user) {

        return ReadingPreference.builder()
                .user(user)
                .mbti(null)
                .favoriteBooks(new HashSet<>())
                .favoriteAuthors(new HashSet<>())
                .moods(new ArrayList<>())
                .readingHabits(new ArrayList<>())
                .genres(new ArrayList<>())
                .keywords(new ArrayList<>())
                .readingStyles(new ArrayList<>())
                .isCompleted(false)
                .build();
    }


}


