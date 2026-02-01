package BookPick.mvp.domain.comment.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import BookPick.mvp.domain.comment.dto.create.CommentCreateReq;
import BookPick.mvp.domain.comment.dto.create.CommentCreateRes;
import BookPick.mvp.domain.comment.dto.delete.CommentDeleteRes;
import BookPick.mvp.domain.comment.entity.Comment;
import BookPick.mvp.domain.comment.exception.CommentNotFoundException;
import BookPick.mvp.domain.comment.repository.CommentRepository;
import BookPick.mvp.domain.curation.entity.Curation;
import BookPick.mvp.domain.curation.exception.common.CurationNotFoundException;
import BookPick.mvp.domain.curation.repository.CurationRepository;
import BookPick.mvp.domain.user.entity.User;
import BookPick.mvp.domain.user.exception.common.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("댓글 서비스 테스트")
class CommentServiceTest {

    @InjectMocks private CommentService commentService;

    @Mock private UserRepository userRepository;

    @Mock private CurationRepository curationRepository;

    @Mock private CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 생성 성공 - 일반 댓글")
    void createComment_success() {
        // given
        Long userId = 1L;
        Long curationId = 1L;
        CommentCreateReq req = new CommentCreateReq(null, "좋은 큐레이션이네요!");

        User mockUser = User.builder().id(userId).email("test@test.com").build();

        Curation mockCuration = Curation.builder().id(curationId).commentCount(0).build();

        Comment mockComment =
                Comment.builder()
                        .id(1L)
                        .user(mockUser)
                        .curation(mockCuration)
                        .content(req.content())
                        .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(curationRepository.findByIdWithLock(curationId)).thenReturn(Optional.of(mockCuration));
        when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);

        // when
        CommentCreateRes result = commentService.createComment(userId, curationId, req);

        // then
        assertThat(result.commentId()).isEqualTo(1L);
        assertThat(mockCuration.getCommentCount()).isEqualTo(1);

        verify(userRepository).findById(userId);
        verify(curationRepository).findByIdWithLock(curationId);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("대댓글 생성 성공")
    void createReply_success() {
        // given
        Long userId = 1L;
        Long curationId = 1L;
        Long parentCommentId = 10L;
        CommentCreateReq req = new CommentCreateReq(parentCommentId, "답글입니다!");

        User mockUser = User.builder().id(userId).build();
        Curation mockCuration = Curation.builder().id(curationId).commentCount(1).build();
        Comment parentComment = Comment.builder().id(parentCommentId).build();
        Comment mockReply =
                Comment.builder()
                        .id(2L)
                        .user(mockUser)
                        .curation(mockCuration)
                        .parent(parentComment)
                        .content(req.content())
                        .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(curationRepository.findByIdWithLock(curationId)).thenReturn(Optional.of(mockCuration));
        when(commentRepository.findById(parentCommentId)).thenReturn(Optional.of(parentComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(mockReply);

        // when
        CommentCreateRes result = commentService.createComment(userId, curationId, req);

        // then
        assertThat(result.commentId()).isEqualTo(2L);
        assertThat(mockCuration.getCommentCount()).isEqualTo(2);

        verify(commentRepository).findById(parentCommentId);
        verify(commentRepository)
                .save(
                        argThat(
                                comment ->
                                        comment.getParent() != null
                                                && comment.getParent()
                                                        .getId()
                                                        .equals(parentCommentId)));
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment_success() {
        // given
        Long curationId = 1L;
        Long commentId = 1L;

        Curation mockCuration = Curation.builder().id(curationId).commentCount(5).build();

        Comment mockComment = Comment.builder().id(commentId).content("댓글 내용").build();

        when(curationRepository.findByIdWithLock(curationId)).thenReturn(Optional.of(mockCuration));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        // when
        CommentDeleteRes result = commentService.deleteComment(curationId, commentId);

        // then
        assertThat(result.commentId()).isEqualTo(commentId);
        assertThat(mockCuration.getCommentCount()).isEqualTo(4);

        verify(curationRepository).findByIdWithLock(curationId);
        verify(commentRepository).delete(mockComment);
    }

    @Test
    @DisplayName("존재하지 않는 유저 - 예외 발생")
    void createComment_userNotFound() {
        // given
        Long userId = 999L;
        Long curationId = 1L;
        CommentCreateReq req = new CommentCreateReq(null, "댓글");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.createComment(userId, curationId, req))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(userId);
        verify(commentRepository, never()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 큐레이션 - 예외 발생")
    void createComment_curationNotFound() {
        // given
        Long userId = 1L;
        Long curationId = 999L;
        CommentCreateReq req = new CommentCreateReq(null, "댓글");

        User mockUser = User.builder().id(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(curationRepository.findByIdWithLock(curationId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.createComment(userId, curationId, req))
                .isInstanceOf(CurationNotFoundException.class);

        verify(commentRepository, never()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 부모 댓글 - 예외 발생")
    void createReply_parentCommentNotFound() {
        // given
        Long userId = 1L;
        Long curationId = 1L;
        Long invalidParentId = 999L;
        CommentCreateReq req = new CommentCreateReq(invalidParentId, "답글");

        User mockUser = User.builder().id(userId).build();
        Curation mockCuration = Curation.builder().id(curationId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(curationRepository.findByIdWithLock(curationId)).thenReturn(Optional.of(mockCuration));
        when(commentRepository.findById(invalidParentId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.createComment(userId, curationId, req))
                .isInstanceOf(CommentNotFoundException.class);

        verify(commentRepository, never()).save(any());
    }

    @Test
    @DisplayName("비관적 락 사용 확인 - 데드락 방지")
    void createComment_usesPessimisticLock() {
        // given
        Long userId = 1L;
        Long curationId = 1L;
        CommentCreateReq req = new CommentCreateReq(null, "댓글");

        User mockUser = User.builder().id(userId).build();
        Curation mockCuration = Curation.builder().id(curationId).commentCount(0).build();
        Comment mockComment = Comment.builder().id(1L).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(curationRepository.findByIdWithLock(curationId)).thenReturn(Optional.of(mockCuration));
        when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);

        // when
        commentService.createComment(userId, curationId, req);

        // then
        verify(curationRepository).findByIdWithLock(curationId); // 비관적 락 사용
        verify(curationRepository, never()).findById(curationId); // 일반 findById 사용 안함
    }

    @Test
    @DisplayName("댓글 삭제 시 카운트가 0 이하로 내려가지 않음")
    void deleteComment_countNotNegative() {
        // given
        Long curationId = 1L;
        Long commentId = 1L;

        Curation mockCuration = Curation.builder().id(curationId).commentCount(0).build();

        Comment mockComment = Comment.builder().id(commentId).build();

        when(curationRepository.findByIdWithLock(curationId)).thenReturn(Optional.of(mockCuration));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        // when
        commentService.deleteComment(curationId, commentId);

        // then
        assertThat(mockCuration.getCommentCount()).isEqualTo(0); // 음수 안됨

        verify(commentRepository).delete(mockComment);
    }
}
