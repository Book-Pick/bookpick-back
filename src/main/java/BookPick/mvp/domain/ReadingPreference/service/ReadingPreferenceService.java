package BookPick.mvp.domain.ReadingPreference.service;

import BookPick.mvp.domain.ReadingPreference.Exception.AlreadyRegisteredReadingPreferenceException;
import BookPick.mvp.domain.ReadingPreference.Exception.UserReadingPreferenceNotExisted;
import BookPick.mvp.domain.ReadingPreference.dto.Create.ReadingPreferenceCreateReq;
import BookPick.mvp.domain.ReadingPreference.dto.Create.ReadingPreferenceCreateRes;
import BookPick.mvp.domain.ReadingPreference.dto.Delete.ReadingPreferenceDeleteRes;
import BookPick.mvp.domain.ReadingPreference.dto.Get.ReadingPreferenceGetReq;
import BookPick.mvp.domain.ReadingPreference.dto.Get.ReadingPreferenceGetRes;
import BookPick.mvp.domain.ReadingPreference.dto.Update.ReadingPreferenceUpdateReq;
import BookPick.mvp.domain.ReadingPreference.dto.Update.ReadingPreferenceUpdateRes;
import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;
import BookPick.mvp.domain.ReadingPreference.repository.ReadingPreferenceRepository;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReadingPreferenceService {
    private final ReadingPreferenceRepository readingPreferenceRepository;
    private final UserRepository userRepository;

    // -- 유저 독서 취향 등록 --
    @Transactional
    public ReadingPreferenceCreateRes addReadingPreference(Long userId, ReadingPreferenceCreateReq req) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (readingPreferenceRepository.existsByUserId(userId)) {
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


        return ReadingPreferenceCreateRes.from(saved);
    }


    // -- 유저 독서 취향 단건 조회 --
    @Transactional
    public ReadingPreferenceGetRes findReadingPreference(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        ReadingPreference result = readingPreferenceRepository.findByUserId(userId)
                .orElseThrow(UserReadingPreferenceNotExisted::new);

        return ReadingPreferenceGetRes.from(result);
    }

    // -- 본인 유저 독서 수정 --
    @Transactional
    public ReadingPreferenceUpdateRes modifyReadingPreference(Long userId, ReadingPreferenceUpdateReq req) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        ReadingPreference preference = readingPreferenceRepository.findByUserId(userId)
                .orElseThrow(UserReadingPreferenceNotExisted::new);

        preference.update(req);

        return ReadingPreferenceUpdateRes.from(preference);
    }

    // -- 본인 유저 독서 삭제 --
    @Transactional
    public ReadingPreferenceDeleteRes removeReadingPreference(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        ReadingPreference preference = readingPreferenceRepository.findByUserId(userId)
                .orElseThrow(UserReadingPreferenceNotExisted::new);

        readingPreferenceRepository.delete(preference);


        return ReadingPreferenceDeleteRes.from(preference.getId(), LocalDateTime.now());

    }

}
