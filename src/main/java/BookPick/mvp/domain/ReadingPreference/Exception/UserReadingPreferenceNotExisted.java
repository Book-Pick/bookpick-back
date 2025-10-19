package BookPick.mvp.domain.ReadingPreference.Exception;

import BookPick.mvp.global.api.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class UserReadingPreferenceNotExisted extends BusinessException {
    public UserReadingPreferenceNotExisted(){
        super(ErrorCode.READING_PREFERENCE_NOT_EXISTED);
    }
}
