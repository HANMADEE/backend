package study.project.backend.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import study.project.backend.domain.user.config.GoogleProperties;
import study.project.backend.domain.user.controller.Platform;
import study.project.backend.domain.user.response.GoogleTokenResponse;
import study.project.backend.domain.user.response.GoogleUserResponse;
import study.project.backend.domain.user.response.UserResponse;
import study.project.backend.global.common.exception.CustomException;

import static study.project.backend.global.common.Result.FAIL;

@Service
@RequiredArgsConstructor
public class GoogleLoginService implements OAuth2LoginService{

    private final PasswordEncoder passwordEncoder;
    private final GoogleProperties googleProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Platform supports() {
        return Platform.GOOGLE;
    }

    // DTO로 변환
    @Override
    public UserResponse.OAuth toSocialEntityResponse(String code, Platform platform) {
        String accessToken = toRequestAccessToken(code);
        GoogleUserResponse profile = toRequestProfile(accessToken);

        return UserResponse.OAuth.builder()
                .email(profile.getEmail())
                .name(profile.getName())
                .build();
    }

    // Google AccessToken 응답
    private String toRequestAccessToken(String code) {
        // 발급받은 code -> GET 요청
        ResponseEntity<GoogleTokenResponse> response = restTemplate.postForEntity(
                googleProperties.getRequestTokenUri(),
                googleProperties.getRequestParameter(code),
                GoogleTokenResponse.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new CustomException(FAIL);
        }

        return response.getBody().getAccessToken();
    }

    // 유저 정보 응답
    private GoogleUserResponse toRequestProfile(String accessToken) {
        // accessToken 헤더 등록
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        // GET 요청으로 유저정보 응답 시도
        ResponseEntity<GoogleUserResponse> response =
                restTemplate.exchange("https://www.googleapis.com/oauth2/v1/userinfo", HttpMethod.GET, request, GoogleUserResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new CustomException(FAIL);
        }

        return response.getBody();
    }
}
