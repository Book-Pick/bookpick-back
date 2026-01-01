package BookPick.mvp.domain.ReadingPreference.Exception.fail;

import BookPick.mvp.global.api.ErrorCode.ErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class UserReadingPreferenceNotExisted extends BusinessException {
    public UserReadingPreferenceNotExisted(){
        super(ErrorCode.READING_PREFERENCE_NOT_EXISTED);
    }
}
