package BookPick.mvp.domain.preference.service;

import BookPick.mvp.domain.Author.entity.Author;
import BookPick.mvp.domain.Author.repository.AuthorRepository;
import BookPick.mvp.domain.Book.entity.Book;
import BookPick.mvp.domain.Book.repository.BookRepository;
import BookPick.mvp.domain.preference.dto.PreferenceDtos.*;
import BookPick.mvp.domain.preference.entity.UserPreference;
import BookPick.mvp.domain.preference.repository.PreferenceRepository;
import BookPick.mvp.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PreferenceService {
    private final PreferenceRepository preferenceRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Transactional
    public PreferenceRes create(Long userId, CreateReq req) {
        User user = new User();
        user.setId(userId);

        if (preferenceRepository.existsByUserId(userId)) {
            throw new IllegalStateException("이미 등록된 선호정보가 있습니다.");
        }

        // --- 문자열 요청 → 엔티티 변환 ---
        List<Author> authors = req.favoriteAuthors().stream()
            .map(name -> authorRepository.findByName((name))
                .orElseGet(() -> authorRepository.save(
                    Author.builder().name(name).build()
                ))
            ).toList();

        List<Book> books = req.favoriteBooks().stream()
            .map(title -> bookRepository.findByTitle((title))
                .orElseGet(() -> bookRepository.save(
                    Book.builder().title(title).build()
                ))
            ).toList();

        // --- UserPreference 생성 ---
        UserPreference pref = UserPreference.from(req, user);
        pref.setFavoriteAuthors(authors);
        pref.setFavoriteBooks(books);

        UserPreference saved = preferenceRepository.save(pref);
        return PreferenceRes.from(saved);
    }
}
