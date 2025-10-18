package BookPick.mvp.domain.ReadingPreference.repository;

import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingPreferenceRepository extends JpaRepository<ReadingPreference, Long> {
    boolean existsByUserId(Long userId);
}
