package BookPick.mvp.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_taste")
@Getter
@Setter
public class UserTaste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자 선호도 ID (PK)

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true) // UNIQUE + FK
    private User user; // user 테이블 참조

    @Column(length = 4)
    private String mbti; // MBTI

    @Column(name = "reading_style", length = 50)
    private String readingStyle; // 독서 스타일

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 시각

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 수정 시각
}
