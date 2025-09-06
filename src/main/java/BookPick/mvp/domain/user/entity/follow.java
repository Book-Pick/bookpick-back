package BookPick.mvp.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "follow")
@Getter
@Setter
public class follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 내부 식별자 (PK)

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower; // 팔로우를 건 유저 (FK → User.user_id)

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User following; // 팔로우 당한 유저 (FK → User.user_id)

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 팔로우 생성 시각
}
