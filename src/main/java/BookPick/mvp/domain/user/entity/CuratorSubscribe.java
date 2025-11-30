package BookPick.mvp.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "curator_subscribe",
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
public class CuratorSubscribe {

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



