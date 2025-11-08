package BookPick.mvp.domain.auth.service;


import BookPick.mvp.domain.auth.Roles;
import BookPick.mvp.domain.user.exception.UserNotFoundException;
import BookPick.mvp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


// 스프링 시큐리티에서 자동으로 호출

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<GrantedAuthority> auth = new ArrayList<>();


        // 유저 찾고
        BookPick.mvp.domain.user.entity.User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);


        // 역할 부여하고
        if (user.getRole().equals(Roles.ROLE_USER)) {
            auth.add(new SimpleGrantedAuthority(Roles.ROLE_USER.name()));
        }

        // 프로바이더가 토큰으로 받은 비밀번호와 비교할 DB에서 조회한 User객체 반환 -> 해당 유저 객체의 비밀번호를 프로바이더가 사용한다.
        CustomUserDetails customUserDetails = new CustomUserDetails(user, auth);  // email, password, authorities 등록
        customUserDetails.setId(user.getId());
        customUserDetails.setNickname(user.getNickname());
        customUserDetails.setBio(user.getBio());
        customUserDetails.setProfileImageUrl(user.getProfileImageUrl());
        customUserDetails.setFirstLogin(user.isFirstLogin());

        return customUserDetails;

    }

}

