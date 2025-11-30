package BookPick.mvp.domain.curation.entity;

import BookPick.mvp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "curation_subscribe",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_curator",
                        columnNames = {"user_id", "curator_id"}
                )
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CurationSubscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curator_id")
    private User curator;
}



