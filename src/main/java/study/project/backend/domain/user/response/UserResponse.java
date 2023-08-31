package study.project.backend.domain.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.project.backend.domain.user.entity.Users;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

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

    @AllArgsConstructor(access = PRIVATE)
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Search {
        private Long userId;
        private String email;
        private String nickName;
        private String profileImageUrl;

        public static Search response(Users user) {
            return Search.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .nickName(user.getNickName())
                    .profileImageUrl(user.getProfileImageUrl())
                    .build();
        }
    }

    @AllArgsConstructor(access = PRIVATE)
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Register {
        private Long id;
        private String nickName;
        private String email;
        private String profileImageUrl;

        public static Register response(Users user) {
            return Register.builder()
                    .id(user.getId())
                    .nickName(user.getNickName())
                    .email(user.getEmail())
                    .profileImageUrl(user.getProfileImageUrl())
                    .build();
        }
    }
}
