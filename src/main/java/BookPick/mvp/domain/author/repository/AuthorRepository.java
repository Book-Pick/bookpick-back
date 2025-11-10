package BookPick.mvp.domain.author.repository;


// 1. 작가 레포가 왜 필요할까?
// 2. 추후 이러한 작가들이 많은 사용자들이 찾더라 저장 필요

import BookPick.mvp.domain.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {


}
