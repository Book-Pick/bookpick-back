// CurationService.java
package BookPick.mvp.domain.curation.service;

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
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    @Transactional(readOnly = true)
    public CurationListGetRes getCurationList(String sort, Long cursor, int size) {
        // 커서 기반 조회 (size + 1개 조회)
        List<Curation> curations;
        if (cursor == null) {
            // 첫 요청 - User JOIN
            PageRequest pageRequest = PageRequest.of(0, size + 1, getSortCondition(sort));
            curations = curationRepository.findAllWithUser(pageRequest).getContent();
        } else {
            // 커서 이후 데이터 조회 - User JOIN
            PageRequest pageRequest = PageRequest.of(0, size + 1);
            curations = switch (sort) {
                case "latest" -> curationRepository.findByIdLessThanWithUserLatest(cursor, pageRequest);
                case "popular" -> curationRepository.findByIdLessThanWithUserPopular(cursor, pageRequest);
                case "views" -> curationRepository.findByIdLessThanWithUserViews(cursor, pageRequest);
                default -> curationRepository.findByIdLessThanWithUserLatest(cursor, pageRequest);
            };
        }

        // hasNext 판단
        boolean hasNext = curations.size() > size;
        if (hasNext) {
            curations = curations.subList(0, size);
        }

        // Entity -> DTO 변환
        List<CurationContentRes> content = curations.stream()
                .map(CurationContentRes::from)
                .toList();

        // nextCursor 계산
        Long nextCursor = hasNext && !content.isEmpty()
                ? content.get(content.size() - 1).curationId()
                : null;

        return new CurationListGetRes(
                sort,
                getSortDescription(sort),
                content,
                hasNext,
                nextCursor
        );
    }

    private Sort getSortCondition(String sort) {
        return switch (sort) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "popular" -> Sort.by(Sort.Direction.DESC, "popularityScore", "id");
            case "views" -> Sort.by(Sort.Direction.DESC, "viewCount", "id");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
    }

    private String getSortDescription(String sort) {
        return switch (sort) {
            case "latest" -> "최신 작성순";
            case "popular" -> "인기순";
            case "views" -> "조회순";
            default -> "최신 작성순";
        };
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