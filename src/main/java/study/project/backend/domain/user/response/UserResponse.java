package study.project.backend.domain.user.response;

import lombok.*;
import study.project.backend.domain.user.entity.Users;

public class UserResponse {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    @Builder
    public static class OAuth {
        private String email;
        private String name;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Login {
        private Long userId;
        private String email;
        private String nickName;
        private String accessToken;
        private String refreshToken;

        public static Login response(Users user, String accessToken, String refreshToken) {
            return Login.builder()
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .nickName(user.getNickName())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }
}
