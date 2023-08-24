package study.project.backend.domain.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserServiceRequest {

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
