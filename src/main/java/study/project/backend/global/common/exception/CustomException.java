package study.project.backend.global.common.exception;

import lombok.Getter;
import lombok.Setter;
import study.project.backend.global.common.Result;

@Getter
@Setter
public class CustomException extends RuntimeException {

	private Result result;
	private String debug;

	public CustomException(Result result) {
		this.result = result;
		this.debug = result.getMessage();
	}
}
