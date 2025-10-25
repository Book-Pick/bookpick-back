// CurationAccessDeniedException.java
package BookPick.mvp.domain.curation.converter.exception;

import BookPick.mvp.global.api.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class CurationAccessDeniedException extends BusinessException {
    public CurationAccessDeniedException() {
        super(ErrorCode.CURATION_ACCESS_DENIED);
    }
}