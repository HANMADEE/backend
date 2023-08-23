package study.project.backend.docs.paper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import study.project.backend.docs.RestDocsSupport;
import study.project.backend.domain.paper.controller.PaperController;
import study.project.backend.domain.paper.request.PaperRequest;
import study.project.backend.domain.paper.response.PaperResponse;
import study.project.backend.domain.paper.service.PaperService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PaperControllerDocsTest extends RestDocsSupport {

    private final PaperService paperService = mock(PaperService.class);

    @Override
    protected Object initController() {
        return new PaperController(paperService);
    }

    @DisplayName("롤링페이퍼 만들기 API")
    @Test
    void createRollingPaper() throws Exception {
        // given
        PaperRequest.Create request = new PaperRequest.Create("생일", null, false, false);

        given(paperService.createRollingPaper(any(), any()))
                .willReturn(
                        PaperResponse.Create.builder()
                                .id(1L)
                                .userId(1L)
                                .subject("생일")
                                .theme("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                                .isOpen(false)
                                .isLikeOpen(false)
                                .build()
                );

        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/paper")
                                .header("Authorization", "token")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("createRollingPaper",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("token")
                        ),
                        requestFields(
                                fieldWithPath("subject").type(STRING).description("주제"),
                                fieldWithPath("theme").type(STRING).description("테마").optional(),
                                fieldWithPath("isOpen").type(BOOLEAN).description("외부 공개 여부"),
                                fieldWithPath("isLikeOpen").type(BOOLEAN).description("좋아요 공개 여부")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(STRING).description("상태 메세지"),
                                fieldWithPath("data.id").type(NUMBER).description("롤링페이퍼 ID"),
                                fieldWithPath("data.userId").type(NUMBER).description("롤링페이퍼 소유자 ID"),
                                fieldWithPath("data.subject").type(STRING).description("주제"),
                                fieldWithPath("data.theme").type(STRING).description("테마 이미지 URL"),
                                fieldWithPath("data.isOpen").type(BOOLEAN).description("외부 공개 여부"),
                                fieldWithPath("data.isLikeOpen").type(BOOLEAN).description("좋아요 공개 여부")
                        )
                ));
    }

}
