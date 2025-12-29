package BookPick.mvp.domain.comment.service;

import BookPick.mvp.domain.comment.dto.read.ReceivedCommentsDTO;
import BookPick.mvp.domain.comment.entity.Comment;
import BookPick.mvp.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceivedCommentsService {
    private final CommentRepository commentRepository;

    public ReceivedCommentsDTO receivedCommentsRead(Long userId){
        List<Comment> comments = commentRepository.findLatestCommentsByUserId(userId);

        return ReceivedCommentsDTO.from(comments);
    }
}
