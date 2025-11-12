package BookPick.mvp.domain.author.entity;

import BookPick.mvp.domain.author.dto.preference.AuthorDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer curated_count;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public Author() {

    }

    public static Author from(AuthorDto dto) {
        return Author.builder()
                .name(dto.name())
                .curated_count(0)    // 초기값 설정
                .createdAt(null)
                .updatedAt(null)
                .deletedAt(null)
                .build();
    }


}
