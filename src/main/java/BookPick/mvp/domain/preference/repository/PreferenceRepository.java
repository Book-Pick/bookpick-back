package BookPick.mvp.domain.preference.repository;

import BookPick.mvp.domain.preference.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<UserPreference, Long> {
    boolean existsByUserId(Long userId);
}
