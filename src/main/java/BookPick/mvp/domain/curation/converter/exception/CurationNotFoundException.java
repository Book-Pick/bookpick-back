// CurationNotFoundException.java
package BookPick.mvp.domain.curation.converter.exception;

import BookPick.mvp.global.api.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class CurationNotFoundException extends BusinessException {
    public CurationNotFoundException() {
        super(ErrorCode.CURATION_NOT_FOUND);
    }
}