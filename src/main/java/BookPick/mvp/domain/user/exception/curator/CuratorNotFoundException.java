package BookPick.mvp.domain.user.exception.curator;

import BookPick.mvp.domain.user.enums.curator.CuratorErrorCode;
import BookPick.mvp.global.api.ErrorCode.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class CuratorNotFoundException extends BusinessException {
    public CuratorNotFoundException(){
        super(CuratorErrorCode.Curator_NOT_FOUND);
    }
}
