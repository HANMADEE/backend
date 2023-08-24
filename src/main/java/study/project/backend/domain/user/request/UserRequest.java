package study.project.backend.domain.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateNickName {
        @NotNull(message = "해당 요청은 닉네임이 필수값 입니다.")
        @NotBlank(message = "요청 값이 비어있습니다.")
        private String nickName;

        public UserServiceRequest.UpdateNickName toServiceRequest() {
            return UserServiceRequest.UpdateNickName.builder()
                    .nickName(nickName)
                    .build();
        }
    }
}
