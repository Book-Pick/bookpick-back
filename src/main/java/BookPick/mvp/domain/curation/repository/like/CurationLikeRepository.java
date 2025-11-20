package BookPick.mvp.domain.curation.repository.like;

import BookPick.mvp.domain.curation.entity.CurationLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CurationLikeRepository extends JpaRepository<CurationLike,Long> {
    Optional<CurationLike> findByUserIdAndCurationId(Long userID, Long curationId);
}
