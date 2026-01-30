// SelfSubscribeDeniedException.java
package BookPick.mvp.domain.curation.exception.common;

import BookPick.mvp.global.api.ErrorCode.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class CurationAccessDeniedException extends BusinessException {
    public CurationAccessDeniedException() {
        super(ErrorCode.CURATION_ACCESS_DENIED);
    }
}
