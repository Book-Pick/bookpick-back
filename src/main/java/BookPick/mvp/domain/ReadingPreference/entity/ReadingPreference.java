package BookPick.mvp.domain.ReadingPreference.entity;

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

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String mbti;

    @ElementCollection
    @CollectionTable(name = "preference_favorite_books", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "book")
    private List<String> favoriteBooks;

    @ElementCollection
    @CollectionTable(name = "preference_moods", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "criteria")
    private List<String> moods;

    @ElementCollection
    @CollectionTable(name = "preference_reading_habits", joinColumns = @JoinColumn(name = "preference_id"))
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

    }


