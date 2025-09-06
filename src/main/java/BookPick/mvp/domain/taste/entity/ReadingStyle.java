package BookPick.mvp.domain.taste.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reading_style")
@Getter
@Setter
public class ReadingStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 리딩 스타일 ID (PK)

    @Column(nullable = false, length = 50)
    private String name; // 리딩 스타일 명

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 시각

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 수정 시각
}
