package study.project.backend.docs.report;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import study.project.backend.docs.RestDocsSupport;
import study.project.backend.domain.report.controller.ReportController;
import study.project.backend.domain.report.request.ReportRequest;
import study.project.backend.domain.report.service.ReportService;

import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReportControllerDocsTest extends RestDocsSupport {

    private final ReportService reportService = mock(ReportService.class);

    @Override
    protected Object initController() {
        return new ReportController(reportService);
    }

    @DisplayName("유저 신고하기 API")
    @Test
    void reportUser() throws Exception {
        // given
        ReportRequest.Report request = new ReportRequest.Report(1L, 1L, "저 사람이 나놀림");

        MockHttpServletRequestBuilder httpRequest = RestDocumentationRequestBuilders.post("/report")
                .header(AUTHORIZATION, "Bearer {token}")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        ResourceSnippetParameters parameters = ResourceSnippetParameters.builder()
                .tag("4. Report API")
                .summary("유저 신고하기 API")
                .requestHeaders(
                        headerWithName("Authorization")
                                .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 " +
                                        "또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .requestFields(
                        fieldWithPath("reportedUserId").type(NUMBER).description("신고할 유저 Id"),
                        fieldWithPath("commentId").type(NUMBER).description("신고할 코멘트 Id"),
                        fieldWithPath("reportedContent").type(STRING).description("신고 내용"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"))
                .build();

        RestDocumentationResultHandler document =
                documentHandler("reportUser", prettyPrint(), prettyPrint(), parameters);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }
}
