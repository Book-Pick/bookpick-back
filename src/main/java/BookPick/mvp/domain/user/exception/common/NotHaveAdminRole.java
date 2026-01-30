package BookPick.mvp.domain.user.exception.common;

import BookPick.mvp.domain.user.enums.user.UserErrorCode;
import BookPick.mvp.global.exception.BusinessException;

public class NotHaveAdminRole extends BusinessException {
    public NotHaveAdminRole() {
        super(UserErrorCode.NOT_HAVE_ADMIN_ROLE);
    }
}
