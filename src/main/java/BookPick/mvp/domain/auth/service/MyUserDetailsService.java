package BookPick.mvp.domain.auth.service;


import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
    private final UserRepository UserRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var customeUserOpt = UserRepo.findByEmail(email);
        List<GrantedAuthority> auth= new ArrayList<>();
        if(customeUserOpt.isEmpty()){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
        }



        if (customeUserOpt.get().getRole().equals(Roles.ROLE_USER)) {
            auth.add(new SimpleGrantedAuthority(Roles.ROLE_USER.name()));
        }

        var customUser = new CustomUser(customeUserOpt.get(), auth);  //username = email, passowrd, authorities 등록
        customUser.setId(customeUserOpt.get().getId());
        customUser.setNickname(customeUserOpt.get().getNickname());
        customUser.setBio(customeUserOpt.get().getBio());
        customUser.setProfileImageUrl(customeUserOpt.get().getProfileImageUrl());

        return customUser;

    }

    @Getter
    @Setter
    public static class CustomUser extends User{
        private Long id;
        private String nickname;
        private String bio;
        private String profileImageUrl;

        public CustomUser(
                BookPick.mvp.domain.user.entity.User user,
                Collection<? extends GrantedAuthority> authorities
        ){
            super(user.getEmail(), user.getPassword(),authorities);
            this.bio = user.getBio();
            this.profileImageUrl=user.getProfileImageUrl();
        }

    }


}

