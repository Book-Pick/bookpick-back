package BookPick.mvp.domain.user.util;

import BookPick.mvp.domain.auth.exception.NotAuthenticateUser;
import BookPick.mvp.domain.auth.service.CustomUserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class CurrentUserCheck {

    public void isValidCurrentUser(CustomUserDetails currentUser) {
        if (currentUser == null) {
            throw new NotAuthenticateUser();
        }
    }
}