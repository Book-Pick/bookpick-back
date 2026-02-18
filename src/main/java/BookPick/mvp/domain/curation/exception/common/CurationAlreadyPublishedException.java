// SelfSubscribeDeniedException.java
package BookPick.mvp.domain.curation.exception.common;

import BookPick.mvp.global.api.ErrorCode.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class CurationAlreadyPublishedException extends BusinessException {
    public CurationAlreadyPublishedException() {
        super(ErrorCode.CURATION_ACCESS_DENIED);
    }
}
