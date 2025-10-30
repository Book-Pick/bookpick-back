package BookPick.mvp.domain.preference.Exception;

import BookPick.mvp.global.api.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class AlreadyRegisteredReadingPreferenceException extends BusinessException {
    public AlreadyRegisteredReadingPreferenceException(){
        super(ErrorCode.READING_PREFERENCE_ALREADY_RESiGSTER);
    }
}
