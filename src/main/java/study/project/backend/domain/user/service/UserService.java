package study.project.backend.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.backend.domain.user.controller.Platform;
import study.project.backend.domain.user.entity.Users;
import study.project.backend.domain.user.repository.UserRepository;
import study.project.backend.domain.user.response.UserResponse;
import study.project.backend.global.common.exception.CustomException;
import study.project.backend.global.config.jwt.TokenProvider;

import java.util.List;

import static study.project.backend.domain.user.entity.Authority.ROLE_USER;
import static study.project.backend.global.common.Result.FAIL;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final List<OAuth2LoginService> oAuth2LoginServices;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Transactional
    public UserResponse.Login googleLogin(String code, Platform platform) {
        // OAuth 로그인 진행
        UserResponse.OAuth socialLoginUser = toSocialLogin(code, platform);
        Users userEntity = Users.builder()
                .email(socialLoginUser.getEmail())
                .nickName(socialLoginUser.getName())
                .password(passwordEncoder.encode(platform.name()))
                .authority(ROLE_USER)
                .build();


        // 서비스 회원이 아니면 가입
        Users user = userRepository.findByEmail(userEntity.getEmail())
                .orElseGet(() -> userRepository.save(userEntity));

        // 토큰 발급
        String accessToken = tokenProvider.createToken(
                user.getUserId(), getAuthentication(user.getEmail(), platform.name())
        );
        String refreshToken = tokenProvider.createRefreshToken(user.getEmail());

        return UserResponse.Login.response(user, accessToken, refreshToken);
    }

    // method

    private UserResponse.OAuth toSocialLogin(String code, Platform platform) {
        UserResponse.OAuth socialLoginUser = null;

        // 인터페이스 구현체를 돌면서 해당되는 플랫폼으로 로그인
        for (OAuth2LoginService oAuth2LoginService : oAuth2LoginServices) {
            if (oAuth2LoginService.supports().equals(platform)) {
                socialLoginUser = oAuth2LoginService.toSocialEntityResponse(code, platform);
                break;
            }
        }

        if (socialLoginUser == null) {
            throw new CustomException(FAIL);
        }

        return socialLoginUser;
    }

    private Authentication getAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
