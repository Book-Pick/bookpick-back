package BookPick.mvp.domain.user.repository.subscribe;

import BookPick.mvp.domain.user.entity.CuratorSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurationSubscribeRepository extends JpaRepository<CuratorSubscribe, Integer> {

    Optional<CuratorSubscribe> findByUserIdAndCuratorId(Long userId, Long curatorId);

}
