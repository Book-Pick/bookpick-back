package BookPick.mvp.domain.ReadingPreference.Exception;

import BookPick.mvp.domain.ReadingPreference.enums.resCode.PreferenceErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class WrongReadingPreferenceRequestException extends BusinessException {
    public WrongReadingPreferenceRequestException(){
        super(PreferenceErrorCode.WRONG_READING_PREFERENCE_REQUEST);
    }
}
