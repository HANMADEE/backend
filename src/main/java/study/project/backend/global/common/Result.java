package study.project.backend.global.common;

import lombok.Getter;

@Getter
public enum Result {

    OK(0, "성공"),
    FAIL(-1, "실패"),

    // User
    NOT_FOUND_USER(-1000, "존재하지 않는 사용자"),
    EMAIL_AND_NICKNAME_NOT_NULL_CONSTRAINT(-1001, "닉네임과 이메일 중 하나는 NULL이 아니어야 합니다."),
    NOT_MATCHED_PASSWORD(-1002, "비밀번호가 일치하지 않습니다."),
    NOT_SOCIAL_LOGIN(-1003, "해당 아이디는 소셜로그인으로 회원가입된 회원입니다."),

    // RollingPaper
    NOT_FOUND_PAPER(-2000, "존재하지 않는 롤링페이퍼"),
    NOT_MY_PAPER(-2001, "내가 만든 롤링페이퍼가 아닙니다."),
    UNSUPPORTED_SORT_OPTION(-2002, "지원하지 않는 정렬 방식입니다."),

    // Comment
    NOT_FOUND_COMMENT(-3000, "존재하지 않는 코멘트"),
    NOT_MY_COMMENT(-3001, "내가 작성한 코멘트가 아닙니다.");

    private final int code;
    private final String message;

    Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
