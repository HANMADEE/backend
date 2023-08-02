package study.project.backend.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.backend.domain.user.entity.Users;
import study.project.backend.domain.user.repository.UserRepository;
import study.project.backend.global.common.exception.CustomException;

import java.util.List;

import static study.project.backend.global.common.Result.NOT_FOUND_USER;

@Service
@Component("userDetailsService")
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) {
        return userRepository.findByEmail(email)
                .map(this::createUser)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    }

    private User createUser(Users user) {

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getAuthority().name());
        List<GrantedAuthority> grantedAuthorities = List.of(simpleGrantedAuthority);
        return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}
