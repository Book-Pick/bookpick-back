package BookPick.mvp.domain.comment.service;

import BookPick.mvp.domain.comment.dto.create.CommentCreateReq;
import org.springframework.stereotype.Component;

@Component
public class CommentPolicy {

    boolean isChildrenComment(CommentCreateReq req) {

        // 댓글만드는데 부모댓글이 존재하면
        if( req.parentId() != null){
            return true;
        }

        return false;
    }
}
