package study.project.backend.domain.user.response;

import lombok.*;
import study.project.backend.domain.user.entity.Users;

import java.util.Optional;

public class UserResponse {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    @Builder
    public static class OAuth {
        private String email;
        private String name;
        private Optional<String> profileImageUrl;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
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
