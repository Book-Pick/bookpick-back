package BookPick.mvp.domain.curation.repository.subscribe;

import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.entity.CurationLike;
import BookPick.mvp.domain.curation.entity.CurationSubscribe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurationSubscribeRepository extends JpaRepository<CurationSubscribe, Integer> {

    Optional<CurationSubscribe> findByUserIdAndCuratorId(Long userId, Long curatorId);

}
