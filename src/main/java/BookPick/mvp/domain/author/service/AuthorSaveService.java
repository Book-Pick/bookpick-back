package BookPick.mvp.domain.author.service;

import BookPick.mvp.domain.author.dto.preference.AuthorDto;
import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthorSaveService {
    private final AuthorRepository authorRepository;

    // 1. Author 리스트
    public void saveAuthorIfNotExists(Set<Author> authors) {
        for (Author author : authors) {
            saveAuthorIfNotExists(author); // 단건 메서드 재사용
        }
    }

    // 2. Author 단건
    public void saveAuthorIfNotExists(Author author) {
        authorRepository.findByName(author.getName())
                .orElseGet(() -> authorRepository.save(author));
    }
    // 3. String 리스트
    public Set<Author> saveAuthorsIfNotExistsByName(Set<String> authorNames) {
        Set<Author> authors= new HashSet<>();

        for (String name : authorNames) {
            authors.add( saveAuthorIfNotExistsByName(name)); // 단건 메서드 재사용
        }

        return authors;
    }

    // 4. String 단건
    public Author saveAuthorIfNotExistsByName(String name) {
        return  authorRepository.findByName(name)
                .orElseGet(() -> authorRepository.save(new Author(null, name, 0, LocalDateTime.now(), null, null)));
    }

    // 5.AuthorDto 리스트
    public Set<Author> saveAuthorIfNotExistsDto(Set<AuthorDto> authorDtos) {
        Set<Author> authors = new HashSet<>();
        for (AuthorDto dto : authorDtos) {
            authors.add(saveAuthorIfNotExistsDto(dto));
        }

        return authors;
    }

    // 6.AuthorDto 단건
    public Author saveAuthorIfNotExistsDto(AuthorDto dto) {
        return authorRepository.findByName(dto.name())
                .orElseGet(() -> authorRepository.save(new Author(null, dto.name(), 0, null, null, null)));
    }
}
