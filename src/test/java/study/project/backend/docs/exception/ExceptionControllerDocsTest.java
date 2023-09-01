package study.project.backend.docs.exception;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import study.project.backend.docs.RestDocsSupport;
import study.project.backend.domain.exception.ExceptionController;
import study.project.backend.domain.exception.ExceptionHandler;
import study.project.backend.global.common.Result;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExceptionControllerDocsTest extends RestDocsSupport {

    private final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

    @Override
    protected Object initController() {
        return new ExceptionController(exceptionHandler);
    }

    @DisplayName("기본 에러코드 문서")
    @Test
    void commonExceptionDocs() throws Exception {
        // given
        given(exceptionHandler.commonException())
                .willReturn("비밀번호는 대문자, 소문자, 특수문자, 숫자를 포함하고 있어야 합니다.");

        ResourceSnippetParameters parameters = ResourceSnippetParameters.builder()
                .tag("0. Exception")
                .summary("Parameter Exception")
                .description("""
                        상태 코드(고정) / 상태 메세지 \s
                        \s
                        -1, 비밀번호는 대문자, 소문자, 특수문자, 숫자를 포함하고 있어야 합니다.
                        """
                )
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"))
                .build();

        RestDocumentationResultHandler document =
                documentHandler("exception1", prettyPrint(), parameters);

        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/common-exception")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document);
    }

    @DisplayName("커스텀 에러코드 문서")
    @Test
    void customExceptionDocs() throws Exception {
        // given
        given(exceptionHandler.customException())
                .willReturn(Result.NOT_FOUND_USER);

        ResourceSnippetParameters parameters = ResourceSnippetParameters.builder()
                .tag("0. Exception")
                .summary("Custom Exception")
                .description("""
                        상태 코드 / 상태 메시지 \s
                        \s
                        -1000, 존재하지 않는 사용자 \s
                        -1001, 닉네임과 이메일 중 하나는 NULL이 아니어야 합니다. \s
                        -1002, 비밀번호가 일치하지 않습니다. \s
                        -1003, 해당 아이디는 소셜로그인으로 회원가입된 회원입니다. \s
                        -1004, 내 계정이 아닙니다. \s
                        \s
                        -2000, 존재하지 않는 롤링페이퍼 \s
                        -2001, 내가 만든 롤링페이퍼가 아닙니다. \s
                        -2002, 지원하지 않는 정렬 방식입니다. \s
                        \s
                        -3000, 존재하지 않는 코멘트 \s
                        -3001, 내가 작성한 코멘트가 아닙니다. \s
                        -3002, 해당 이메일은 소셜로그인으로 진행해야 합니다. \s
                        """
                )
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"))
                .build();

        RestDocumentationResultHandler document =
                documentHandler("exception2", prettyPrint(), parameters);

        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/custom-exception")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document);
    }
}
