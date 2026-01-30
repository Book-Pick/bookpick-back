package BookPick.mvp.domain.ReadingPreference.Exception.fail;

import BookPick.mvp.global.api.ErrorCode.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class AlreadyRegisteredReadingPreferenceException extends BusinessException {
    public AlreadyRegisteredReadingPreferenceException() {
        super(ErrorCode.READING_PREFERENCE_ALREADY_RESiGSTER);
    }
}
