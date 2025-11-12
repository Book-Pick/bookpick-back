package BookPick.mvp.domain.user.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import springboot.kakao_boot_camp.domain.user.enums.UserRole;

import java.util.Collection;

@Component
public class AdminManager {

    public boolean isAdmin(Collection<GrantedAuthority> authorities){
        for (GrantedAuthority authority : authorities){
            if(authority.getAuthority().equals(UserRole.ROLE_ADMIN.name())){
                return true;
            }
        }
        return false;
    }
}
