package study.project.backend.docs.comment;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import study.project.backend.docs.RestDocsSupport;
import study.project.backend.domain.comment.controller.CommentController;
import study.project.backend.domain.comment.request.CommentRequest;
import study.project.backend.domain.comment.request.CommentServiceRequest;
import study.project.backend.domain.comment.response.CommentResponse;
import study.project.backend.domain.comment.service.CommentService;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerDocsTest extends RestDocsSupport {

    CommentService commentService = mock(CommentService.class);

    @Override
    protected Object initController() {
        return new CommentController(commentService);
    }

    @DisplayName("한마디 작성 API")
    @Test
    void createComment() throws Exception {
        // given
        CommentRequest.Create request = new CommentRequest.Create(
                "생일축하해 한마디야!", "default.png", "godic", "center", "white", "친구"
        );

        given(commentService.createComment(anyLong(),any(CommentServiceRequest.Create.class),any()))
                .willReturn(
                        CommentResponse.Create.builder()
                                .id(1L)
                                .paperId(1L)
                                .userId(1L)
                                .userName("한마디")
                                .content("호찹아! 너가 그립다 잘지내지!")
                                .imageUrl("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                                .font("godic")
                                .sort("center")
                                .backgroundColor("white")
                                .kind("지인")
                                .build()
                );

        MockHttpServletRequestBuilder httpRequest = RestDocumentationRequestBuilders.post("/paper/{paperId}/comment", 1L)
                .header(AUTHORIZATION, "optional / Bearer {token} ")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        ResourceSnippetParameters parameters = ResourceSnippetParameters.builder()
                .tag("코멘트(한마디) API")
                .summary("한마디 작성 API")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 " +
                                "또는 Authorize 버튼을 이용해 토큰을 넣어주세요")
                        .optional())
                .pathParameters(
                        parameterWithName("paperId").description("롤링페이퍼 ID"))
                .requestFields(
                        fieldWithPath("content").type(STRING).description("코멘트 내용"),
                        fieldWithPath("imageUrl").type(STRING).description("코멘트 이미지").optional(),
                        fieldWithPath("font").type(STRING).description("코멘트 폰트"),
                        fieldWithPath("sort").type(STRING).description("코멘트 정렬"),
                        fieldWithPath("backgroundColor").type(STRING).description("코멘트 배경색"),
                        fieldWithPath("kind").type(STRING).description("관계 유형"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data.id").type(NUMBER).description("코멘트 ID"),
                        fieldWithPath("data.paperId").type(NUMBER).description("롤링페이퍼 ID"),
                        fieldWithPath("data.userId").type(NUMBER).description("유저 ID"),
                        fieldWithPath("data.userName").type(STRING).description("유저 이름"),
                        fieldWithPath("data.userName").type(STRING).description("유저 이름"),
                        fieldWithPath("data.content").type(STRING).description("코멘트 내용"),
                        fieldWithPath("data.imageUrl").type(STRING).description("코멘트 이미지"),
                        fieldWithPath("data.font").type(STRING).description("코멘트 폰트"),
                        fieldWithPath("data.sort").type(STRING).description("코멘트 정렬방식"),
                        fieldWithPath("data.backgroundColor").type(STRING).description("코멘트 배경색"),
                        fieldWithPath("data.kind").type(STRING).description("코멘트 관계 유형"))
                .requestSchema(Schema.schema("CommentRequest.Create"))
                .responseSchema(Schema.schema("CommentResponse.Create"))
                .build();

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("createComment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(parameters)));
    }

}