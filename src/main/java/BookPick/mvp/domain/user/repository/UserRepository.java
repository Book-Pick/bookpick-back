package BookPick.mvp.domain.user.repository;

import BookPick.mvp.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Object findFirstByEmail(String email);

    boolean existsAllByEmail(String email);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
