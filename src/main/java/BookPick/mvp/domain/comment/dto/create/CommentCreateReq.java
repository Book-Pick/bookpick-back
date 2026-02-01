package BookPick.mvp.domain.comment.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Todo. 각 리코드 분리 필요
// -- C --
public record CommentCreateReq(
        Long parentId,
        @NotBlank(message = "댓글 내용은 비워둘 수 없습니다.")
                @Size(max = 1000, message = "댓글은 최대 1000자까지 입력 가능합니다.")
                String content) {}
