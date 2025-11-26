package BookPick.mvp.domain.curation.repository.subscribe;

import BookPick.mvp.domain.curation.entity.CurationLike;
import BookPick.mvp.domain.curation.entity.CurationSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurationSubscribeRepository extends JpaRepository<CurationSubscribe, Integer> {

    Optional<CurationSubscribe> findByUserIdAndCurationId(Long userId, Long curationId);
}
