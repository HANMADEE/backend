package study.project.backend.global.common;

import lombok.Getter;

@Getter
public enum Result {

    OK(0, "성공"),
    FAIL(-1, "실패"),

    // User
    NOT_FOUND_USER(-1000, "존재하지 않는 사용자"),
    EMAIL_AND_NICKNAME_NOT_NULL_CONSTRAINT(-1001, "닉네임과 이메일 중 하나는 NULL이 아니어야 합니다."),

    // RollingPaper
    NOT_FOUND_PAPER(-2000, "존재하지 않는 롤링페이퍼");

    private final int code;
    private final String message;

    Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
