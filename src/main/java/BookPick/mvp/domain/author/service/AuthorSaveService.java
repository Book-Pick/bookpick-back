package BookPick.mvp.domain.author.service;

import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorSaveService {
    private final AuthorRepository authorRepository;

    // 1.독서취향에서 작가 꺼내서, 작가가 db에 등록되어있지 않으면 저장
    public void saveIfNotExists(List<Author> authors) {
        List<Author> preferredAuthors = authors;
        for (Author preferredAuthor : preferredAuthors) {
            Optional<Author> existingAuthor = authorRepository.findByName(preferredAuthor.getName());
            if (existingAuthor.isEmpty()) {
                authorRepository.save(preferredAuthor);
            }
        }
    }

}
