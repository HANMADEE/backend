package study.project.backend.domain.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import study.project.backend.global.common.CustomResponseEntity;
import study.project.backend.global.common.Result;

@RestController
@RequiredArgsConstructor
public class ExceptionController {

    private final ExceptionHandler exceptionHandler;

    @DeleteMapping("/common-exception")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomResponseEntity<String> commonException () {
        return CustomResponseEntity.fail(exceptionHandler.commonException());
    }

    @DeleteMapping("/custom-exception")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomResponseEntity<Result> customException () {
        return CustomResponseEntity.fail(exceptionHandler.customException());
    }
}
