package BookPick.mvp.domain.comment.dto.read;

import BookPick.mvp.domain.comment.entity.Comment;

import java.util.List;
import java.util.stream.Collectors;

public record ReceivedCommentsDTO(
        List<CommentDetailRes> comments
) {
    public static ReceivedCommentsDTO from(List<Comment> comments){
        List<CommentDetailRes> commentDetailResList = comments.stream()
                .map(CommentDetailRes::of)
                .collect(Collectors.toList());

        return new ReceivedCommentsDTO(commentDetailResList);
    }
}
