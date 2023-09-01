package study.project.backend.domain.exception;

import org.springframework.stereotype.Service;
import study.project.backend.global.common.Result;

@Service
public class ExceptionHandler {
    public String commonException() {
        return "비밀번호는 대문자, 소문자, 특수문자, 숫자를 포함하고 있어야 합니다.";
    }

    public Result customException() {
        return Result.NOT_FOUND_USER;
    }
}
