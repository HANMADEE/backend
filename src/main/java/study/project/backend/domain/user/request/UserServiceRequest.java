package study.project.backend.domain.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

public class UserServiceRequest {

    @AllArgsConstructor
    @Getter
    @Builder
    public static class Register {
        private String nickName;
        private String email;
        private String password;
        private String profileImageUrl;

        public Optional<String> getProfileImageUrl() {
            return Optional.ofNullable(profileImageUrl);
        }
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class UpdateNickName {
        private String nickName;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class Search {
        private String nickName;
        private String email;
    }
}
