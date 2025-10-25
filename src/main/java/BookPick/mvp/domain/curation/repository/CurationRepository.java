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


    @Query("SELECT c FROM Curation c WHERE c.id <= :cursor ORDER BY c.createdAt DESC, c.id DESC")
    List<Curation> findByCursorWithSize(@Param("cursor") Long cursor, Pageable pageable);
}

