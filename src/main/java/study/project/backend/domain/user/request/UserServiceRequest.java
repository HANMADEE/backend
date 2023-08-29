package study.project.backend.domain.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

public class UserServiceRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Update {
        private String nickName;
        private String email;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Login {
        private String email;
        private String password;
    }

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
