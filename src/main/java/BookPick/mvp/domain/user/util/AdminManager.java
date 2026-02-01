package BookPick.mvp.domain.user.util;

import BookPick.mvp.domain.auth.Roles;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class AdminManager {

    public boolean isAdmin(Collection<GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(Roles.ROLE_ADMIN.name())) {
                return true;
            }
        }
        return false;
    }
}
