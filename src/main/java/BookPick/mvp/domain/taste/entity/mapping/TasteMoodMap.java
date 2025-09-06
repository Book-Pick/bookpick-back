package BookPick.mvp.domain.taste.entity.mapping;

import BookPick.mvp.domain.taste.entity.Mood;
import BookPick.mvp.domain.user.entity.UserTaste;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "taste_mood")
@Getter
@Setter
public class TasteMoodMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 식별자 (PK)

    @ManyToOne
    @JoinColumn(name = "taste_id", nullable = false)
    private UserTaste userTaste; // 선호도 ID (FK → user_taste)

    @ManyToOne
    @JoinColumn(name = "mood_id", nullable = false)
    private Mood mood; // 분위기 ID (FK → mood)

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 시각
}
