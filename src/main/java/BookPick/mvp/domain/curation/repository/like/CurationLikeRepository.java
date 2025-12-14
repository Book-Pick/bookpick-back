package BookPick.mvp.domain.curation.repository.like;

import BookPick.mvp.domain.curation.entity.CurationLike;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface CurationLikeRepository extends JpaRepository<CurationLike, Long> {
    Optional<CurationLike> findByUserIdAndCurationId(Long userID, Long curationId);

    List<CurationLike> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Optional<CurationLike> findByUserId(Long userId);


    // CurationLikeRepository
    @Query("""
            select cl.curation.id
            from CurationLike cl
            where cl.user.id = :userId
            and cl.curation.id in :curationIds
            """)
    List<Long> findLikedCurationIds(Long userId, List<Long> curationIds);

}
