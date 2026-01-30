package BookPick.mvp.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import BookPick.mvp.domain.comment.dto.read.ReceivedCommentsDTO;
import BookPick.mvp.domain.comment.entity.Comment;
import BookPick.mvp.domain.comment.repository.CommentRepository;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.user.entity.User;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("받은 댓글 서비스 테스트")
class ReceivedCommentsServiceTest {

    @InjectMocks private ReceivedCommentsService receivedCommentsService;

    @Mock private CommentRepository commentRepository;

    @Test
    @DisplayName("사용자가 받은 최신 댓글 3개 조회")
    void receivedCommentsRead_success() {
        // given
        Long userId = 1L;
        User user = User.builder().id(userId).build();
        Curation curation = Curation.builder().id(1L).build(); // Mock Curation
        Comment comment1 =
                Comment.builder().id(1L).content("Comment 1").user(user).curation(curation).build();
        Comment comment2 =
                Comment.builder().id(2L).content("Comment 2").user(user).curation(curation).build();
        Comment comment3 =
                Comment.builder().id(3L).content("Comment 3").user(user).curation(curation).build();
        List<Comment> comments = List.of(comment1, comment2, comment3);

        when(commentRepository.findLatestCommentsByUserId(userId, PageRequest.of(0, 3)))
                .thenReturn(comments);

        // when
        ReceivedCommentsDTO result = receivedCommentsService.receivedCommentsRead(userId);

        // then
        assertThat(result.comments()).hasSize(3);
        assertThat(result.comments().get(0).content()).isEqualTo("Comment 1");
    }

    @Test
    @DisplayName("받은 댓글이 없을 경우 빈 리스트 반환")
    void receivedCommentsRead_noComments() {
        // given
        Long userId = 1L;
        when(commentRepository.findLatestCommentsByUserId(userId, PageRequest.of(0, 3)))
                .thenReturn(Collections.emptyList());

        // when
        ReceivedCommentsDTO result = receivedCommentsService.receivedCommentsRead(userId);

        // then
        assertThat(result.comments()).isEmpty();
    }
}
