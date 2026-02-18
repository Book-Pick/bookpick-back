package BookPick.mvp.domain.auth.service;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
public class CustomUserDetails extends User {
    private Long id;
    private String nickname;
    private String bio;
    private String profileImageUrl;
    private boolean isFirstLogin;

    public CustomUserDetails(
            BookPick.mvp.domain.user.entity.User user,
            Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.bio = user.getBio();
        this.profileImageUrl = user.getProfileImageUrl();
    }

    public CustomUserDetails(
            Long userId, String email, Collection<? extends GrantedAuthority> authorities) {
        super(email, "", authorities);
        this.id = userId;
    }

    public static CustomUserDetails fromJwt(
            Long userId, String email, Collection<? extends GrantedAuthority> authorities) {
        return new CustomUserDetails(userId, email, authorities);
    }
}
