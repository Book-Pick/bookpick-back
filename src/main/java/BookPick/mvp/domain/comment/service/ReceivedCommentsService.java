package BookPick.mvp.domain.comment.service;

import BookPick.mvp.domain.comment.dto.read.ReceivedCommentsDTO;
import BookPick.mvp.domain.comment.entity.Comment;
import BookPick.mvp.domain.comment.repository.CommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReceivedCommentsService {
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public ReceivedCommentsDTO receivedCommentsRead(Long userId) {
        List<Comment> comments =
                commentRepository.findLatestCommentsByUserId(userId, PageRequest.of(0, 3));

        return ReceivedCommentsDTO.from(comments);
    }
}
