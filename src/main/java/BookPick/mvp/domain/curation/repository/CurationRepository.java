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

    List<Curation> findByUserIdAndIsDraftedOrderByCreatedAtDesc(Long userId, boolean isDrafted, Pageable pageable);


    // 사이즈만큼 최신순으로 불러오는 함수
    List<Curation> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Drafted 여부에 따라 가져옴
    List<Curation> findAllByIsDraftedOrderByCreatedAtDesc(Boolean isDrafted, Pageable pageable);


    @Query("SELECT c FROM Curation c WHERE c.id <= :cursor and c.isDrafted = :isDrafted order BY c.createdAt DESC, c.id DESC")
    List<Curation> findLatestCurations(@Param("cursor") Long cursor,
                                       @Param("isDrafted") boolean isDrafted,
                                       Pageable pageable);

    // 인기순
    @Query("""
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
            Pageable pageable
    );

    // Gemini 추천 결과로 큐레이션 찾기
    @Query("""
            SELECT DISTINCT c FROM Curation c
            LEFT JOIN c.moods m
            LEFT JOIN c.genres g
            LEFT JOIN c.keywords k
            LEFT JOIN c.styles s
            WHERE c.deletedAt IS NULL and c.isDrafted is false and c.user.id != :userId
            AND (m IN :moods OR g IN :genres OR k IN :keywords OR s IN :styles)
            ORDER BY c.popularityScore DESC
            """)
    List<Curation> findPublishedCurationsByRecommendation(
            @Param("userId") Long userId,
            @Param("moods") List<String> moods,
            @Param("genres") List<String> genres,
            @Param("keywords") List<String> keywords,
            @Param("styles") List<String> styles
    );


    Optional<CurationLike> findByUserIdAndId(Long userId, Long id);


    // 7.
    @Query("SELECT c FROM Curation c JOIN FETCH c.user WHERE c.id = :id")
    Optional<Curation> findByIdWithUser(@Param("id") Long id);

    List<Curation> findByIdIn(Collection<Long> ids);


    // Like (실제 발행된 것만)
    @Query("""
                select c from Curation c
                join CurationLike cl on  cl.curation = c
                where c.isDrafted is false and c.user.id = :userId
                order by cl.createdAt desc
            """)
    List<Curation> findLikedCurationsByUser(@Param("userId") Long userId, Pageable pageable);

}

