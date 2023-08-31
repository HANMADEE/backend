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
import study.project.backend.domain.user.request.UserServiceRequest;
import study.project.backend.domain.user.response.UserResponse;
import study.project.backend.global.common.Result;
import study.project.backend.global.common.exception.CustomException;
import study.project.backend.global.config.jwt.TokenProvider;

import java.util.List;

import static study.project.backend.domain.user.entity.Authority.ROLE_USER;
import static study.project.backend.global.common.Result.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final List<OAuth2LoginService> oAuth2LoginServices;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 API
    @Transactional
    public UserResponse.Register register(UserServiceRequest.Register request) {
        validateRegister(request);
        Users user = userRepository.save(Users.builder()
                .authority(ROLE_USER)
                .nickName(request.getNickName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .profileImageUrl(request.getProfileImageUrl().orElse("default.png"))
                .build());

        return UserResponse.Register.response(user);
    }

    // 로그인 API
    public UserResponse.Login login(UserServiceRequest.Login request) {
        Users user = getUser(request.getEmail());
        validatePassword(request, user);

        // 토큰 발급
        String accessToken = tokenProvider.createToken(
                user.getId(), getAuthentication(request.getEmail(), request.getPassword())
        );
        String refreshToken = tokenProvider.createRefreshToken(user.getEmail());

        return UserResponse.Login.response(user, accessToken, refreshToken);
    }

    // 소셜로그인 API
    @Transactional
    public UserResponse.Login socialLogin(String code, Platform platform) {
        // OAuth 로그인 진행
        UserResponse.OAuth socialLoginUser = toSocialLogin(code, platform);
        Users userEntity = Users.builder()
                .email(socialLoginUser.getEmail())
                .nickName(socialLoginUser.getName())
                .profileImageUrl(socialLoginUser.getProfileImageUrl().orElse("default.png"))
                .password(passwordEncoder.encode(platform.name()))
                .authority(ROLE_USER)
                .build();


        // 서비스 회원이 아니면 가입
        Users user = userRepository.findByEmail(userEntity.getEmail())
                .orElseGet(() -> userRepository.save(userEntity));

        // 토큰 발급
        String accessToken = tokenProvider.createToken(
                user.getId(), getAuthentication(user.getEmail(), platform.name())
        );
        String refreshToken = tokenProvider.createRefreshToken(user.getEmail());

        return UserResponse.Login.response(user, accessToken, refreshToken);
    }

    // 닉네임 수정 API
    @Transactional
    public Void updateNickName(UserServiceRequest.UpdateNickName request, Long userId) {
        Users user = getUser(userId);
        user.toUpdateNickname(request.getNickName());
        return null;
    }

    // 유저 검색 API
    public List<UserResponse.Search> searchUsers(UserServiceRequest.Search request) {
        if (request.getEmail() == null && request.getNickName() == null) {
            throw new CustomException(EMAIL_AND_NICKNAME_NOT_NULL_CONSTRAINT);
        }

        List<Users> users = userRepository.findAllByKeyword(request.getNickName(), request.getEmail());
        return users.stream()
                .map(UserResponse.Search::response)
                .toList();
    }

    // 내 정보 조회 API
    public UserResponse.Search readUser(Long userId) {
        return UserResponse.Search.response(getUser(userId));
    }

    // 내 정보 수정 API
    public Void updateUser(Long userId, UserServiceRequest.Update request) {
        Users user = getUser(userId);
        user.toUpdate(request.getNickName(), request.getEmail());

        return null;
    }

    @Transactional
    public Void updatePassword(Long userId, UserServiceRequest.UpdatePassword request) {
        Users user = getUser(userId);
        validateUpdatePassword(request, user, userId);

        user.toUpdatePassword(passwordEncoder.encode(request.getNewPassword()));
        return null;
    }


    // method
    private void validateUpdatePassword(UserServiceRequest.UpdatePassword request, Users user, Long userId) {
        if (!user.getId().equals(userId)) {
            throw new CustomException(NOT_MY_ACCOUNT);
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new CustomException(NOT_MATCHED_PASSWORD);
        }

        if (!request.getNewPassword().equals(request.getCheckNewPassword())) {
            throw new CustomException(NOT_MATCHED_PASSWORD);
        }
    }

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

    private Users getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER)
        );
    }

    private Users getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER)
        );
    }

    private Authentication getAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private void validatePassword(UserServiceRequest.Login request, Users user) {
        String enteredPassword = request.getPassword();

        if (enteredPassword.equals("kakao") || enteredPassword.equals("KAKAO") || enteredPassword.equals("google") || enteredPassword.equals("GOOGLE")) {
            throw new CustomException(NOT_SOCIAL_LOGIN);
        }

        if (!passwordEncoder.matches(enteredPassword, user.getPassword())) {
            throw new CustomException(NOT_MATCHED_PASSWORD);
        }
    }

    private static void validateRegister(UserServiceRequest.Register request) {
        if (request.getEmail().contains("gmail") || request.getEmail().contains("daum")) {
            throw new CustomException(NOT_REGISTER_SOCIAL);
        }
    }
}
