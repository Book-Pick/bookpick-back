package BookPick.mvp.domain.ReadingPreference.service;

import BookPick.mvp.domain.ReadingPreference.Exception.AlreadyRegisteredReadingPreferenceException;
import BookPick.mvp.domain.ReadingPreference.Exception.UserReadingPreferenceNotExisted;
import BookPick.mvp.domain.ReadingPreference.dto.ETC.Delete.ReadingPreferenceDeleteRes;
import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceReq;
import BookPick.mvp.domain.ReadingPreference.dto.ReadingPreferenceRes;
import BookPick.mvp.domain.ReadingPreference.entity.ReadingPreference;
import BookPick.mvp.domain.ReadingPreference.repository.ReadingPreferenceRepository;
import BookPick.mvp.domain.author.entity.Author;
import BookPick.mvp.domain.author.service.AuthorSaveService;
import BookPick.mvp.domain.book.dto.preference.BookDto;
import BookPick.mvp.domain.book.entity.Book;
import BookPick.mvp.domain.book.repository.BookRepository;
import BookPick.mvp.domain.book.service.BookSaveService;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReadingPreferenceService {
    private final ReadingPreferenceRepository readingPreferenceRepository;
    private final UserRepository userRepository;
    private final BookSaveService bookSaveService;
    private final AuthorSaveService authorSaveService;
    private final BookRepository bookRepository;


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

        // 3. 독서취향 기반 책 저장 (중복 체크)
        Set<Book> savedBooks = bookSaveService.saveBookIfNotExistsDto(req.favoriteBooks());


        // 4. 독서취향 기반 작가 저장 (중복 체크)
        Set<Author> savedAuthors = authorSaveService.saveAuthorIfNotExistsDto(req.favoriteAuthors());

        // 5. 책 찾고


        ReadingPreference readingPreference = ReadingPreference.builder()
                .user(user)
                .mbti(req.mbti())
                .favoriteBooks(savedBooks)
                .favoriteAuthors(savedAuthors)
                .moods(req.moods())
                .readingHabits(req.readingHabits())
                .genres(req.genres())
                .readingStyles(req.readingStyles())
                .keywords(req.keywords())
                .build();

        ReadingPreference saved = readingPreferenceRepository.save(readingPreference);


        return ReadingPreferenceRes.from(saved);
    }
    // -- 빈 독서취향 등록 --
     @Transactional
    public ReadingPreferenceRes addClearReadingPreference(Long userId) {


        // 1. 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 2. 독서취향이 이미 존재하면 이미 존재하는 독서취향입니다.
        if (readingPreferenceRepository.existsByUserId(userId)) {
            throw new AlreadyRegisteredReadingPreferenceException();
        }

        // 3. 처음 가입한 유저도 독서취향 설정할 수 있게, 회원가입시 빈 독서취향 등록하는 로직 추가
        ReadingPreference saved = readingPreferenceRepository.save(ReadingPreference.clearPreferences(user));

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


//        // 1. 책 저장
//        List<BookDto> bookDtos = req.favoriteBooks();
//        for (BookDto bookDto : bookDtos){
//
//
//            Book book  = bookRepository.findBy
//        }

        // 2. 작가 저장

        // Todo 2. 구현 필요, Service 파라미터로 주면 안돼요!
//        preference.update(req, authorSaveService, bookSaveService);

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
