package BookPick.mvp.domain.book.repository;

// 1. 책 레포 필요한 이유
// 2. 추후 어떠한 책들이 인기가 있었는지 체크하기 위해
// 3. 큐레이션 작성된 책들 count 필요

import BookPick.mvp.domain.book.entity.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitle(String title);
}
