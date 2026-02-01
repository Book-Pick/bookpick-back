// SelfSubscribeDeniedException.java
package BookPick.mvp.domain.user.exception.subscribe;

import BookPick.mvp.global.api.ErrorCode.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class SelfSubscribeDeniedException extends BusinessException {
    public SelfSubscribeDeniedException() {
        super(ErrorCode.CURATION_ACCESS_DENIED);
    }
}
