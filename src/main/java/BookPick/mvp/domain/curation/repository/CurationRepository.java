package BookPick.mvp.domain.curation.repository;

import BookPick.mvp.domain.curation.entity.Curation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CurationRepository extends JpaRepository<Curation, Long> {

    List<Curation> findByUserId(Long userId);


    // 사이즈만큼 최신순으로 불러오는 함수
    List<Curation> findAllByOrderByCreatedAtDesc(Pageable pageable);



    @Query("SELECT c FROM Curation c WHERE c.id <= :cursor ORDER BY c.createdAt DESC, c.id DESC")
    List<Curation> findCurations(@Param("cursor") Long cursor, Pageable pageable);

    @Query("SELECT c FROM Curation c " +
            "WHERE (:cursor IS NULL OR c.id < :cursor) " +  // 이 부분!
            "ORDER BY c.popularityScore DESC, c.id DESC")
    List<Curation> findCurationsByPopularity(@Param("cursor") Long cursor, Pageable pageable);
}

