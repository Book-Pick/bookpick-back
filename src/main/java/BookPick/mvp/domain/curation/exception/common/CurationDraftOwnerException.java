// SelfSubscribeDeniedException.java
package BookPick.mvp.domain.curation.exception.common;

import BookPick.mvp.global.api.ErrorCode.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class CurationDraftOwnerException extends BusinessException {
    public CurationDraftOwnerException() {
        super(ErrorCode.CURATION_DRAFT_ACCESS_DENIED);
    }
}