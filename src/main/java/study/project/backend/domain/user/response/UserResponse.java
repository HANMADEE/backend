package study.project.backend.domain.user.response;

import lombok.*;
import study.project.backend.domain.user.entity.Users;

import java.util.Optional;

import static lombok.AccessLevel.*;

public class UserResponse {

    @AllArgsConstructor(access = PRIVATE)
    @NoArgsConstructor
    @Getter
    @Builder
    public static class OAuth {
        private String email;
        private String name;
        private String profileImageUrl;

        public Optional<String> getProfileImageUrl() {
            return Optional.ofNullable(profileImageUrl);
        }
    }

    @AllArgsConstructor(access = PRIVATE)
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Login {
        private Long userId;
        private String email;
        private String nickName;
        private String profileImageUrl;
        private String accessToken;
        private String refreshToken;

        public static Login response(Users user, String accessToken, String refreshToken) {
            return Login.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .nickName(user.getNickName())
                    .profileImageUrl(user.getProfileImageUrl())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }
}
