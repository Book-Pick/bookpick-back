package BookPick.mvp.domain.ReadingPreference.service;

import BookPick.mvp.domain.ReadingPreference.Exception.AlreadyRegisteredReadingPreferenceException;
import BookPick.mvp.domain.ReadingPreference.Exception.UserReadingPreferenceNotExisted;
import BookPick.mvp.domain.ReadingPreference.dto.ETC.Delete.ReadingPreferenceDeleteRes;
import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceReq;
import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceRes;
import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;
import BookPick.mvp.domain.ReadingPreference.repository.ReadingPreferenceRepository;
import BookPick.mvp.domain.author.service.AuthorSaveService;
import BookPick.mvp.domain.book.service.BookSaveService;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReadingPreferenceService {
    private final ReadingPreferenceRepository readingPreferenceRepository;
    private final UserRepository userRepository;
    private final BookSaveService bookSaveService;
    private final AuthorSaveService authorSaveService;




    // -- 유저 독서 취향 등록 --
    @Transactional
    public ReadingPreferenceRes addReadingPreference(Long userId, ReadingPreferenceReq req) {


        // 1. 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 2. 독서취향이 이미 존재하면 이미 존재하는 독서취향입니다.
        // Todo 1. 독서취향 생성은 처음 회원가입시 바로 생성되고 null값 넣는 것으로 변경 필요
        if (readingPreferenceRepository.existsByUserId(userId)) {
            throw new AlreadyRegisteredReadingPreferenceException();
        }

        bookSaveService.saveIfNotExists(req.favoriteBooks());
        authorSaveService.saveIfNotExists(req.favoriteAuthors());


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


        return ReadingPreferenceRes.from(saved);
    }


    // -- 유저 독서 취향 단건 조회 --
    @Transactional
    public ReadingPreferenceRes findReadingPreference(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        ReadingPreference result = readingPreferenceRepository.findByUserId(userId)
                .orElseThrow(UserReadingPreferenceNotExisted::new);

        return ReadingPreferenceRes.from(result);
    }

    // -- 본인 유저 독서 수정 --
    @Transactional
    public ReadingPreferenceRes modifyReadingPreference(Long userId, ReadingPreferenceReq req) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        ReadingPreference preference = readingPreferenceRepository.findByUserId(userId)
                .orElseThrow(UserReadingPreferenceNotExisted::new);

        preference.update(req);

        return ReadingPreferenceRes.from(preference);
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
