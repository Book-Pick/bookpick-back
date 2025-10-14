package BookPick.mvp.domain.preference.service;

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

    @Transactional
    public PreferenceRes create(Long userId, CreateReq req) {
        User user = new User();
        user.setId(userId);

        if (preferenceRepository.existsByUserId(userId)) {
            throw new IllegalStateException("이미 등록된 선호정보가 있습니다.");
        }

        // --- 문자열 요청 → 엔티티 변환 ---
        List<String> authors = req.favoriteAuthors();  // ["1", "2", "3"]
        List<String> books   = req.favoriteBooks();    // ["1", "2", "3"]

        // --- UserPreference 생성 ---
        UserPreference pref = UserPreference.from(req, user);
        pref.setFavoriteAuthors(authors);
        pref.setFavoriteBooks(books);

        UserPreference saved = preferenceRepository.save(pref);
        return PreferenceRes.from(saved);
    }
}
