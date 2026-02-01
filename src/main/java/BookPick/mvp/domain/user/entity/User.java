package BookPick.mvp.domain.user.entity;

import BookPick.mvp.domain.auth.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_Id")
    private Long id; // 내부 식별자 (PK)

    @Column(name = "login_email", nullable = false, unique = true, length = 255)
    @Email(message = "올바른 이메일 형식이여야 합니다.")
    private String email; // 로그인 ID, 고유

    @Column(name = "login_password", nullable = false, length = 255)
    private String password; // 비밀번호 해시

    @Column(length = 50)
    @Size(min = 2, max = 10, message = "닉네임은 2~10자여야 합니다.")
    private String nickname; // 프로필 닉네임

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Roles role; // ROLE_USER, ROLE_ADMIN 등

    @Column(length = 255)
    @Size(message = "자기소개는 255자 이하여야 합니다.")
    private String bio; // 자기소개 문구

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl; // 프로필 사진 경로

    @Column(name = "is_first_login", nullable = false)
    @Builder.Default
    private boolean isFirstLogin = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 시각

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 수정 시각

    @Column(name = "deleted")
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // 삭제 시각

    public void isNotFirstLogin() {
        this.isFirstLogin = false;
    }
}
