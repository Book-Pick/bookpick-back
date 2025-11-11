package BookPick.mvp.domain.ReadingPreference.service;

import BookPick.mvp.domain.ReadingPreference.Exception.AlreadyRegisteredReadingPreferenceException;
import BookPick.mvp.domain.ReadingPreference.Exception.UserReadingPreferenceNotExisted;
import BookPick.mvp.domain.ReadingPreference.dto.Create.ReadingPreferenceCreateReq;
import BookPick.mvp.domain.ReadingPreference.dto.Create.ReadingPreferenceCreateRes;
import BookPick.mvp.domain.ReadingPreference.dto.Delete.ReadingPreferenceDeleteRes;
import BookPick.mvp.domain.ReadingPreference.dto.Get.ReadingPreferenceGetRes;
import BookPick.mvp.domain.ReadingPreference.dto.Update.ReadingPreferenceUpdateReq;
import BookPick.mvp.domain.ReadingPreference.dto.Update.ReadingPreferenceUpdateRes;
import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;
import BookPick.mvp.domain.ReadingPreference.repository.ReadingPreferenceRepository;
import BookPick.mvp.domain.author.repository.AuthorRepository;
import BookPick.mvp.domain.book.entity.Book;
import BookPick.mvp.domain.book.repository.BookRepository;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReadingPreferenceService {
    private final ReadingPreferenceRepository readingPreferenceRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;


    // -- 유저 독서 취향 등록 --
    @Transactional
    public ReadingPreferenceCreateRes addReadingPreference(Long userId, ReadingPreferenceCreateReq req) {

        // 1. 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 2. 독서취향이 이미 존재하면 이미 존재하는 독서취향입니다.
        // Todo 1. 독서취향 생성은 처음 회원가입시 바로 생성되고 null값 넣는 것으로 변경 필요
        if (readingPreferenceRepository.existsByUserId(userId)) {
            throw new AlreadyRegisteredReadingPreferenceException();
        }

        // 3. 독서취향에서 책 꺼내서, 첵 db에 책 등록되어있지 않으면 저장. 등록되어있으면 가져오기
        List<Book> preferedBooks = req.favoriteBooks();
        for (Book preferedBook : preferedBooks){
           Optional<Book> existingBook = bookRepository.findByTitle(preferedBook.getTitle());
           if(existingBook==null){
               bookRepository.save(preferedBook);
           }
           else{
               
           }
        }


        // 4. 독서취향에서 작가 꺼내서, 작가가 db에 등록되어있지 않으면 저장, 등록되어 있으면 가져오기
        String userPreferAuthorName = req





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
