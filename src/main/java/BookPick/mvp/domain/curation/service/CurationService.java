// CurationService.java
package BookPick.mvp.domain.curation.service;

import BookPick.mvp.domain.curation.SortType;
import BookPick.mvp.domain.curation.dto.create.CurationCreateReq;
import BookPick.mvp.domain.curation.dto.create.CurationCreateRes;
import BookPick.mvp.domain.curation.dto.get.list.CurationContentRes;
import BookPick.mvp.domain.curation.dto.get.list.CurationListGetRes;
import BookPick.mvp.domain.curation.dto.get.one.CurationGetRes;
import BookPick.mvp.domain.curation.dto.update.CurationUpdateReq;
import BookPick.mvp.domain.curation.dto.update.CurationUpdateRes;
import BookPick.mvp.domain.curation.dto.delete.CurationDeleteRes;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.converter.exception.CurationAccessDeniedException;
import BookPick.mvp.domain.curation.converter.exception.CurationNotFoundException;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.curation.service.fetcher.CurationFetcher;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurationService {

    private final CurationRepository curationRepository;
    private final UserRepository userRepository;
    private final CurationFetcher curationFetcher;


    // -- 큐레이션 등록 --
    @Transactional
    public CurationCreateRes create(Long userId, CurationCreateReq req) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);


        Curation curation = Curation.builder()
                .user(user)
                .thumbnailUrl(req.thumbnail().imageUrl())
                .thumbnailColor(req.thumbnail().imageColor())
                .bookTitle(req.book().title())
                .bookAuthor(req.book().author())
                .bookIsbn(req.book().isbn())
                .review(req.review())
                .moods(req.recommend().moods())
                .genres(req.recommend().genres())
                .keywords(req.recommend().keywords())
                .styles(req.recommend().styles())
                .build();

        Curation saved = curationRepository.save(curation);

        return CurationCreateRes.from(saved);
    }


    // -- 큐레이션 단건 조회 --
    @Transactional
    public CurationGetRes findCuration(Long curationId, HttpServletRequest req) {
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(CurationNotFoundException::new);

        curation.increaseViewCount();

        return CurationGetRes.from(curation);
    }


    // -- 큐레이션 목록 조회 --
    public CurationListGetRes getCurationList(SortType sortType, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size + 1);

        List<Curation> curations = curationFetcher.fetchCurations(sortType, cursor, pageable);

        boolean hasNext = curations.size() > size;
        Long nextCursor = curationFetcher.calculateNextCursor(curations, size, hasNext);
        List<Curation> result = hasNext ? curations.subList(0, size) : curations;

        List<CurationContentRes> content = result.stream()
                .map(CurationContentRes::from)
                .toList();

        return CurationListGetRes.from(sortType, content, hasNext, nextCursor);
    }


    // -- 큐레이션 수정 --
    @Transactional
    public CurationUpdateRes modifyCuration(Long userId, Long curationId, CurationUpdateReq req) {
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(CurationNotFoundException::new);

        if (!curation.getUser().getId().equals(userId)) {
            throw new CurationAccessDeniedException();
        }

        curation.update(req);

        return CurationUpdateRes.from(curation);
    }

    // -- 큐레이션 삭제 --
    @Transactional
    public CurationDeleteRes removeCuration(Long userId, Long curationId) {
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(CurationNotFoundException::new);

        if (!curation.getUser().getId().equals(userId)) {
            throw new CurationAccessDeniedException();
        }

        curationRepository.delete(curation);

        return CurationDeleteRes.from(curation.getId(), LocalDateTime.now());
    }
}
