package study.project.backend.domain.user.request;

import com.sun.jdi.request.StepRequest;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.project.backend.global.common.Result;
import study.project.backend.global.common.exception.CustomException;

import static study.project.backend.global.common.Result.*;

public class UserRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Login {

        @NotNull(message = "이메일은 필수 값 입니다.")
        private String email;

        @NotNull(message = "비밀번호는 필수 값 입니다.")
        private String password;

        public UserServiceRequest.Login toServiceRequest() {
            return UserServiceRequest.Login.builder()
                    .email(email)
                    .password(password)
                    .build();
        }
    }

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

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Search {
        private String nickName;
        private String email;

        public UserServiceRequest.Search toServiceRequest() {
            return UserServiceRequest.Search.builder()
                    .nickName(nickName)
                    .email(email)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Register {
        @NotNull(message = "별명은 필수 값 입니다.")
        private String nickName;

        @NotNull(message = "이메일은 필수 값 입니다.")
        @NotBlank(message = "이메일은 비어있을 수 없습니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        private String email;

        @NotNull(message = "비밀번호는 필수 값 입니다.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "비밀번호는 대문자, 소문자, 특수문자, 숫자를 포함하고 있어야 합니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상, 최대 20자 이하여야 합니다.")
        private String password;

        private String profileImageUrl;

        public UserServiceRequest.Register toServiceRequest() {
            return UserServiceRequest.Register.builder()
                    .nickName(nickName)
                    .email(email)
                    .password(password)
                    .profileImageUrl(profileImageUrl)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Update {

        @NotNull(message = "별명은 필수 값 입니다.")
        private String nickName;

        @NotNull(message = "이메일은 필수 값 입니다.")
        @NotBlank(message = "이메일은 비어있을 수 없습니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        private String email;

        public UserServiceRequest.Update toServiceRequest() {
            return UserServiceRequest.Update.builder()
                    .nickName(nickName)
                    .email(email)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdatePassword {
        @NotNull(message = "이전 비밀번호는 필수 값 입니다.")
        private String oldPassword;

        @NotNull(message = "새로운 비밀번호는 필수 값 입니다.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "비밀번호는 대문자, 소문자, 특수문자, 숫자를 포함하고 있어야 합니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상, 최대 20자 이하여야 합니다.")
        private String newPassword;

        @NotNull(message = "새로운 비밀번호 확인은 필수 값 입니다.")
        private String checkNewPassword;

        public UserServiceRequest.UpdatePassword toServiceRequest() {
            return UserServiceRequest.UpdatePassword.builder()
                    .oldPassword(oldPassword)
                    .newPassword(newPassword)
                    .checkNewPassword(checkNewPassword)
                    .build();
        }
    }
}
