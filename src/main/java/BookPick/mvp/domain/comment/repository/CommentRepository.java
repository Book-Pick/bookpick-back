package BookPick.mvp.domain.comment.repository;

import BookPick.mvp.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.parent LEFT JOIN FETCH c.user WHERE c.curation.id = :curationId AND c.deletedAt IS NULL")
    Page<Comment> findByCurationId(@Param("curationId") Long curationId, Pageable pageable);


    @Query("""
                SELECT cm FROM Comment cm
                WHERE cm.curation.user.id = :userId AND cm.deletedAt IS NULL
                ORDER BY cm.createdAt DESC
            """)
    List<Comment> findLatestCommentsByUserId(
            @Param("userId") Long userId,
            Pageable pageable
    );

}
