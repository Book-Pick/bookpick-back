package BookPick.mvp.domain.curation.repository;

import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.entity.CurationLike;
import BookPick.mvp.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CurationRepository extends JpaRepository<Curation, Long> {

    List<Curation> findByUserId(Long userId, Pageable pageable);


    // 사이즈만큼 최신순으로 불러오는 함수
    List<Curation> findAllByOrderByCreatedAtDesc(Pageable pageable);


    @Query("SELECT c FROM Curation c WHERE c.id <= :cursor ORDER BY c.createdAt DESC, c.id DESC")
    List<Curation> findLatestCurations(@Param("cursor") Long cursor, Pageable pageable);

    @Query("SELECT c FROM Curation c " +
            "WHERE (:cursor IS NULL) " +
            "   OR c.popularityScore <= (SELECT c2.popularityScore FROM Curation c2 WHERE c2.id = :cursor) " +
            "   OR (c.popularityScore = (SELECT c2.popularityScore FROM Curation c2 WHERE c2.id = :cursor) AND c.id < :cursor) " +
            "ORDER BY c.popularityScore DESC, c.id DESC")
    List<Curation> findCurationsByPopularity(@Param("cursor") Long cursor, Pageable pageable);

    // Gemini 추천 결과로 큐레이션 찾기
    @Query("""
            SELECT DISTINCT c FROM Curation c
            LEFT JOIN c.moods m
            LEFT JOIN c.genres g
            LEFT JOIN c.keywords k
            LEFT JOIN c.styles s
            WHERE c.deletedAt IS NULL
            AND (m IN :moods OR g IN :genres OR k IN :keywords OR s IN :styles)
            ORDER BY c.popularityScore DESC
            """)
    List<Curation> findByRecommendation(
            @Param("moods") List<String> moods,
            @Param("genres") List<String> genres,
            @Param("keywords") List<String> keywords,
            @Param("styles") List<String> styles
    );

    Optional<CurationLike> findByUserIdAndId(Long userId, Long id);

    Long user(User user);


    // 7.
    @Query("SELECT c FROM Curation c JOIN FETCH c.user WHERE c.id = :id")
    Optional<Curation> findByIdWithUser(@Param("id") Long id);

    List<Curation> findByIdIn(Collection<Long> ids);
}

