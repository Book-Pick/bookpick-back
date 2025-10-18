package BookPick.mvp.domain.ReadingPreference.service;

import BookPick.mvp.domain.ReadingPreference.Exception.AlreadyRegisteredReadingPreferenceException;
import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceDtos.*;
import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;
import BookPick.mvp.domain.ReadingPreference.repository.ReadingPreferenceRepository;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingPreferenceService {
    private final ReadingPreferenceRepository readingPreferenceRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReadingPreferenceRegisterRes create(Long userId, ReadingPreferenceRegisterReq req) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if(readingPreferenceRepository.existsByUserId(userId)){
            throw new AlreadyRegisteredReadingPreferenceException();
        }


        ReadingPreference readingPreference = ReadingPreference.builder()
                .user(user)
                .mbti(req.mbti())
                .favoriteBooks(req.favoriteBooks())
                .moods(req.moods())
                .readingHabits(req.readingHabits())
                .genres(req.genres())
                .trends(req.trends())
                .keywords(req.keywords())
                .build();

        ReadingPreference saved = readingPreferenceRepository.save(readingPreference);


        return ReadingPreferenceRegisterRes.from(saved);
    }
}
