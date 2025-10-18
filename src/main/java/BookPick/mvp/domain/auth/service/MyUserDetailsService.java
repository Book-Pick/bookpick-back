package BookPick.mvp.domain.auth.service;


import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.user.exception.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.coyote.http11.Constants.a;


// 스프링 시큐리티에서 자동으로 호출

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<GrantedAuthority> auth = new ArrayList<>();

        BookPick.mvp.domain.user.entity.User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);


        if (user.getRole().equals(Roles.ROLE_USER)) {
            auth.add(new SimpleGrantedAuthority(Roles.ROLE_USER.name()));
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user, auth);  // email, passWord, authorities 등록
        customUserDetails.setId(user.getId());
        customUserDetails.setNickname(user.getNickname());
        customUserDetails.setBio(user.getBio());
        customUserDetails.setProfileImageUrl(user.getProfileImageUrl());
        customUserDetails.setFirstLogin(user.isFirstLogin());

        return customUserDetails;

    }

    @Getter
    @Setter
    public static class CustomUserDetails extends User {
        private Long id;
        private String nickname;
        private String bio;
        private String profileImageUrl;
        private boolean isFirstLogin;

        public CustomUserDetails(
                BookPick.mvp.domain.user.entity.User user,
                Collection<? extends GrantedAuthority> authorities
        ) {
            super(user.getEmail(), user.getPassword(), authorities);
            this.bio = user.getBio();
            this.profileImageUrl = user.getProfileImageUrl();
        }

        readingㄴㄴㄴ

    }
}

