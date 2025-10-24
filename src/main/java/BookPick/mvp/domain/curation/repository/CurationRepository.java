// CurationRepository.java
package BookPick.mvp.domain.curation.repository;

import BookPick.mvp.domain.curation.entity.Curation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CurationRepository extends JpaRepository<Curation, Long> {
    List<Curation> findByUserId(Long userId);
}