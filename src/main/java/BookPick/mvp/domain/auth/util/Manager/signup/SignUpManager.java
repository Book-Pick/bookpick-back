package BookPick.mvp.domain.auth.util.Manager.signup;

import org.springframework.stereotype.Component;

@Component
public class SignUpManager {

    private String amdinEmail = "admin@admin.com";

    public boolean isAdmin(Object object) {
        if (object.equals(amdinEmail)) {
            return true;
        }
        return false;
    }
}
