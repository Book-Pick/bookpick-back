package BookPick.mvp.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; // 내부 식별자 (PK)

    @Column(name = "login_email", nullable = false, unique = true, length = 255)
    private String email; // 로그인 ID, 고유

    @Column(name = "login_password", nullable = false, length = 255)
    private String password; // 비밀번호 해시

    @Column(length = 50)
    private String nickname; // 프로필 닉네임

    @Column(length = 20)
    private String role;  // ROLE_USER, ROLE_ADMIN 등

    @Column(length = 255)
    private String bio; // 자기소개 문구

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl; // 프로필 사진 경로

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 시각

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 수정 시각
}
