package BookPick.mvp.domain.curation.repository;

import BookPick.mvp.domain.curation.entity.Curation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurationRepository extends JpaRepository<Curation, Long> {

    List<Curation> findByUserId(Long userId);

    // 첫 요청 - User JOIN (정렬 조건은 Pageable로 받음)
    @Query("SELECT c FROM Curation c JOIN FETCH c.user")
    Page<Curation> findAllWithUser(Pageable pageable);

    // 커서 기반 조회 - 최신순
    @Query("SELECT c FROM Curation c JOIN FETCH c.user WHERE c.id < :cursor ORDER BY c.createdAt DESC, c.id DESC")
    List<Curation> findByIdLessThanWithUserLatest(@Param("cursor") Long cursor, Pageable pageable);

    // 커서 기반 조회 - 인기순
    @Query("SELECT c FROM Curation c JOIN FETCH c.user WHERE c.id < :cursor ORDER BY c.popularityScore DESC, c.id DESC")
    List<Curation> findByIdLessThanWithUserPopular(@Param("cursor") Long cursor, Pageable pageable);

    // 커서 기반 조회 - 조회순
    @Query("SELECT c FROM Curation c JOIN FETCH c.user WHERE c.id < :cursor ORDER BY c.viewCount DESC, c.id DESC")
    List<Curation> findByIdLessThanWithUserViews(@Param("cursor") Long cursor, Pageable pageable);
}