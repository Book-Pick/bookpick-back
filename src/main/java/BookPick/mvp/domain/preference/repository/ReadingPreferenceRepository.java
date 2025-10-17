package BookPick.mvp.domain.preference.repository;

import BookPick.mvp.domain.preference.entity.ReadingPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingPreferenceRepository extends JpaRepository<ReadingPreference, Long> {
    boolean existsByUserId(Long userId);
}
