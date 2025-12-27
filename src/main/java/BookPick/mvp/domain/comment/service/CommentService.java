package BookPick.mvp.domain.comment.service;

import BookPick.mvp.domain.comment.dto.create.CommentCreateReq;
import BookPick.mvp.domain.comment.dto.create.CommentCreateRes;
import BookPick.mvp.domain.comment.dto.delete.CommentDeleteRes;
import BookPick.mvp.domain.comment.dto.read.CommentDetailRes;
import BookPick.mvp.domain.comment.dto.read.CommentListRes;
import BookPick.mvp.domain.comment.dto.update.CommentUpdateReq;
import BookPick.mvp.domain.comment.dto.update.CommentUpdateRes;
import BookPick.mvp.domain.comment.entity.Comment;
import BookPick.mvp.domain.comment.exception.CommentNotFoundException;
import BookPick.mvp.domain.comment.repository.CommentRepository;
import BookPick.mvp.domain.curation.exception.common.CurationNotFoundException;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import BookPick.mvp.global.dto.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final CurationRepository curationRepository;
    private final CommentRepository commentRepository;


    // -- Create --
    @Transactional
    public CommentCreateRes createComment(Long userId, Long curationId, CommentCreateReq req) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(CurationNotFoundException::new);

        Comment parent = null;

        if (req.parentId() != null) {
            parent = commentRepository.findById(req.parentId())
                    .orElseThrow(CommentNotFoundException::new);
        }

        Comment comment = Comment.builder()
                .user(user)
                .parent(parent)
                .curation(curation)
                .content(req.content())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Comment saved = commentRepository.save(comment);
        curation.increaseCommentCount();

        return CommentCreateRes.from(saved);
    }


    // -- Read --
    @Transactional(readOnly = true)
    public CommentListRes getCommentList(Long curationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<Comment> commentPage = commentRepository.findByCurationId(curationId, pageable);

        List<CommentListRes.CommentSummary> commentList = commentPage.getContent().stream()
                .map(comment -> CommentListRes.CommentSummary.of(
                        comment.getId(),
                        comment.getUser().getId(),
                        comment.getParent() != null ? comment.getParent().getId() : null,
                        comment.getUser().getNickname(),
                        comment.getUser().getProfileImageUrl(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt()
                ))
                .toList();

        PageInfo pageInfo = PageInfo.of(commentPage);

        return CommentListRes.of(commentList, pageInfo);
    }

    @Transactional(readOnly = true)
    public CommentDetailRes getCommentDetail(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        return CommentDetailRes.of(comment);
    }


    // -- Update --
    @Transactional
    public CommentUpdateRes updateComment(Long commentId, CommentUpdateReq req) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (req.content() != null && !req.content().isBlank()) {
            comment.setContent(req.content());
            comment.setUpdatedAt(LocalDateTime.now());
        }

        return CommentUpdateRes.of(comment);
    }


    // -- Delete --
    @Transactional
    public CommentDeleteRes deleteComment(Long curationId, Long commentId) {
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(CurationNotFoundException::new);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        commentRepository.delete(comment);
        curation.decreaseCommentCount();

        return CommentDeleteRes.of(commentId, LocalDateTime.now());
    }


}
