package BookPick.mvp.domain.curation.repository;

import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.entity.CurationLike;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CurationRepository extends JpaRepository<Curation, Long> {

    List<Curation> findByUserIdAndIsDraftedOrderByCreatedAtDesc(
            Long userId, boolean isDrafted, Pageable pageable);

    // 사이즈만큼 최신순으로 불러오는 함수
    List<Curation> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Drafted 여부에 따라 가져옴
    List<Curation> findAllByIsDraftedOrderByCreatedAtDesc(Boolean isDrafted, Pageable pageable);

    @Query(
            "SELECT c FROM Curation c WHERE c.id <= :cursor and c.isDrafted = :isDrafted order BY"
                    + " c.createdAt DESC, c.id DESC")
    List<Curation> findLatestCurations(
            @Param("cursor") Long cursor, @Param("isDrafted") boolean isDrafted, Pageable pageable);

    // 인기순
    @Query(
            """
                SELECT c FROM Curation c
                WHERE c.isDrafted = false AND
                      (:cursorScore IS NULL
                       OR c.popularityScore < :cursorScore
                       OR (c.popularityScore = :cursorScore AND c.id < :cursorId))
                ORDER BY c.popularityScore DESC, c.id DESC
            """)
    List<Curation> findCurationsByPopularity(
            @Param("cursorScore") Integer cursorScore,
            @Param("cursorId") Long cursorId,
            Pageable pageable);

    // 태그 매칭 사전 필터링 + 후보 수 제한 (EXISTS 서브쿼리로 행 증식 방지)
    @Query(
            """
            SELECT c FROM Curation c
            JOIN FETCH c.user
            WHERE c.deletedAt IS NULL AND c.isDrafted = false AND c.user.id != :userId
            AND (
                EXISTS (SELECT 1 FROM Curation c2 JOIN c2.moods m WHERE c2 = c AND m IN :moods)
                OR EXISTS (SELECT 1 FROM Curation c3 JOIN c3.genres g WHERE c3 = c AND g IN :genres)
                OR EXISTS (SELECT 1 FROM Curation c4 JOIN c4.keywords k WHERE c4 = c AND k IN :keywords)
                OR EXISTS (SELECT 1 FROM Curation c5 JOIN c5.styles s WHERE c5 = c AND s IN :styles)
            )
            ORDER BY c.popularityScore DESC
            """)
    List<Curation> findPublishedCurationsByRecommendation(
            @Param("userId") Long userId,
            @Param("moods") List<String> moods,
            @Param("genres") List<String> genres,
            @Param("keywords") List<String> keywords,
            @Param("styles") List<String> styles,
            Pageable pageable);

    Optional<CurationLike> findByUserIdAndId(Long userId, Long id);

    // 7.
    @Query("SELECT c FROM Curation c JOIN FETCH c.user WHERE c.id = :id")
    Optional<Curation> findByIdWithUser(@Param("id") Long id);

    List<Curation> findByIdIn(Collection<Long> ids);

    // Like (실제 발행된 것만)
    @Query(
            """
                select c from Curation c
                join CurationLike cl on  cl.curation = c
                where c.isDrafted is false and cl.user.id = :userId
                order by cl.createdAt desc
            """)
    List<Curation> findLikedCurationsByUser(@Param("userId") Long userId, Pageable pageable);

    // Pessimistic lock for preventing deadlocks when updating comment count
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Curation c WHERE c.id = :id")
    Optional<Curation> findByIdWithLock(@Param("id") Long id);

    // Pessimistic lock with user join for view count updates
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Curation c JOIN FETCH c.user WHERE c.id = :id")
    Optional<Curation> findByIdWithUserAndLock(@Param("id") Long id);

    // 순수 매칭용: 발행된 큐레이션 전체 조회 (본인 제외)
    // Note: ElementCollection은 Batch Fetch로 처리 (application.yml에서 설정)
    @Query("""
            SELECT DISTINCT c FROM Curation c
            JOIN FETCH c.user
            WHERE c.deletedAt IS NULL
            AND c.isDrafted = false
            AND c.user.id != :userId
            ORDER BY c.popularityScore DESC
            """)
    List<Curation> findAllPublishedExcludeUser(@Param("userId") Long userId);
}
