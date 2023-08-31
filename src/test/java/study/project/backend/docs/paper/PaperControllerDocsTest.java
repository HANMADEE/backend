package study.project.backend.docs.paper;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import study.project.backend.docs.RestDocsSupport;
import study.project.backend.domain.paper.controller.PaperController;
import study.project.backend.domain.paper.request.PaperRequest;
import study.project.backend.domain.paper.response.PaperResponse;
import study.project.backend.domain.paper.service.PaperService;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
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

        MockHttpServletRequestBuilder httpRequest = RestDocumentationRequestBuilders.post("/paper")
                .header(AUTHORIZATION, "Bearer {token}")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        ResourceSnippetParameters parameters = ResourceSnippetParameters.builder()
                .tag("롤링페이퍼 API")
                .summary("롤링페이퍼 만들기 API")
                .requestHeaders(
                        headerWithName("Authorization")
                                .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 " +
                                        "또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .requestFields(
                        fieldWithPath("subject").type(STRING).description("주제"),
                        fieldWithPath("theme").type(STRING).description("테마").optional(),
                        fieldWithPath("isOpen").type(BOOLEAN).description("외부 공개 여부"),
                        fieldWithPath("isLikeOpen").type(BOOLEAN).description("좋아요 공개 여부"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data.id").type(NUMBER).description("롤링페이퍼 ID"),
                        fieldWithPath("data.userId").type(NUMBER).description("롤링페이퍼 소유자 ID"),
                        fieldWithPath("data.subject").type(STRING).description("주제"),
                        fieldWithPath("data.theme").type(STRING).description("테마 이미지 URL"),
                        fieldWithPath("data.isOpen").type(BOOLEAN).description("외부 공개 여부"),
                        fieldWithPath("data.isLikeOpen").type(BOOLEAN).description("좋아요 공개 여부"))
                .requestSchema(Schema.schema("PaperRequest.Create"))
                .responseSchema(Schema.schema("PaperResponse.Create"))
                .build();

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("createRollingPaper",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(parameters)));
    }

    @DisplayName("롤링페이퍼 선물하기 API")
    @Test
    void giftRollingPaper() throws Exception {
        // given
        given(paperService.giftRollingPaper(anyLong(), anyLong(), any()))
                .willReturn(
                        PaperResponse.Create.builder()
                                .id(1L)
                                .userId(3L)
                                .subject("생일")
                                .theme("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                                .isOpen(false)
                                .isLikeOpen(false)
                                .build()
                );

        MockHttpServletRequestBuilder httpRequest = RestDocumentationRequestBuilders.patch("/paper/gift")
                .header(AUTHORIZATION, "Bearer {token}")
                .param("paperId", "1")
                .param("giftedUserId", "3");

        ResourceSnippetParameters parameters = ResourceSnippetParameters.builder()
                .tag("롤링페이퍼 API")
                .summary("롤링페이퍼 선물하기 API")
                .requestHeaders(
                        headerWithName("Authorization")
                                .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 " +
                                        "또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .formParameters(
                        parameterWithName("paperId").description("롤링페이퍼 ID"),
                        parameterWithName("giftedUserId").description("선물받을 유저 ID"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data.id").type(NUMBER).description("롤링페이퍼 ID"),
                        fieldWithPath("data.userId").type(NUMBER).description("롤링페이퍼 소유자 ID"),
                        fieldWithPath("data.subject").type(STRING).description("주제"),
                        fieldWithPath("data.theme").type(STRING).description("테마 이미지 URL"),
                        fieldWithPath("data.isOpen").type(BOOLEAN).description("외부 공개 여부"),
                        fieldWithPath("data.isLikeOpen").type(BOOLEAN).description("좋아요 공개 여부"))
                .requestSchema(Schema.schema("FormParameter-giftRollingPaper"))
                .responseSchema(Schema.schema("PaperResponse.Create"))
                .build();


        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("giftRollingPaper",
                        preprocessResponse(prettyPrint()),
                        resource(parameters)));
    }

    @DisplayName("롤링페이퍼 조회 API")
    @Test
    void readRollingPaper() throws Exception {
        // given

        PaperResponse.Comments comments1 = PaperResponse.Comments.builder()
                .id(1L)
                .userName("한마디")
                .content("생일 축하해 호찹아! 생일빵은 잊지 않았지??")
                .imageUrl("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                .font("godic")
                .sort("center")
                .backgroundColor("white")
                .kind("친구")
                .build();

        PaperResponse.Comments comments2 = PaperResponse.Comments.builder()
                .id(2L)
                .userName("두마디")
                .content("호찹아 우린 언제 만나??")
                .imageUrl("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                .font("godic")
                .sort("center")
                .backgroundColor("white")
                .kind("친구")
                .build();

        PaperResponse.Comments comments3 = PaperResponse.Comments.builder()
                .id(3L)
                .userName("한마디")
                .content("호찹아!! 너랑 같이 메이플 하던게 기억나..")
                .imageUrl("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                .font("godic")
                .sort("center")
                .backgroundColor("white")
                .kind("친구")
                .build();

        given(paperService.readRollingPaper(anyLong()))
                .willReturn(
                        PaperResponse.Read.builder()
                                .id(1L)
                                .userId(1L)
                                .subject("생일")
                                .theme("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                                .isOpen(true)
                                .isLikeOpen(false)
                                .comments(List.of(comments1, comments2, comments3))
                                .build()
                );

        MockHttpServletRequestBuilder httpRequest =
                RestDocumentationRequestBuilders.get("/paper/{paperId}", 1L);

        ResourceSnippetParameters parameters = ResourceSnippetParameters.builder()
                .tag("롤링페이퍼 API")
                .summary("롤링페이퍼 조회 API")
                .pathParameters(
                        parameterWithName("paperId").description("롤링페이퍼 ID"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data.id").type(NUMBER).description("롤링페이퍼 ID"),
                        fieldWithPath("data.userId").type(NUMBER).description("유저 ID"),
                        fieldWithPath("data.subject").type(STRING).description("주제"),
                        fieldWithPath("data.theme").type(STRING).description("테마"),
                        fieldWithPath("data.isOpen").type(BOOLEAN).description("전체 공개 여부"),
                        fieldWithPath("data.isLikeOpen").type(BOOLEAN).description("좋아요 공개 여부"),
                        fieldWithPath("data.comments[]").type(ARRAY).description("롤링페이퍼 코멘트 리스트"),
                        fieldWithPath("data.comments[].id").type(NUMBER).description("롤링페이퍼 코멘트 리스트"),
                        fieldWithPath("data.comments[].userName").type(STRING).description("코멘트 작성자"),
                        fieldWithPath("data.comments[].content").type(STRING).description("코멘트 내용"),
                        fieldWithPath("data.comments[].imageUrl").type(STRING).description("코멘트 이미지"),
                        fieldWithPath("data.comments[].font").type(STRING).description("코멘트 폰트"),
                        fieldWithPath("data.comments[].sort").type(STRING).description("코멘트 정렬방식"),
                        fieldWithPath("data.comments[].backgroundColor").type(STRING).description("코멘트 배경색"),
                        fieldWithPath("data.comments[].kind").type(STRING).description("코멘트 작성자 타입"))
                .build();

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("readRollingPaper",
                        preprocessResponse(prettyPrint()),
                        resource(parameters)));
    }

    @DisplayName("내 롤링페이퍼 조회 API")
    @Test
    void readMyRollingPaper() throws Exception {
        // given

        PaperResponse.SimpleRead simpleRead1 = new PaperResponse.SimpleRead(
                1L, "생일", "https://example.s3.ap-northeast-2.amazonaws.com/image/default.png", false, false, 25
        );

        PaperResponse.SimpleRead simpleRead2 = new PaperResponse.SimpleRead(
                2L, "입사", "https://example.s3.ap-northeast-2.amazonaws.com/image/default.png", true, true, 17
        );

        PaperResponse.SimpleRead simpleRead3 = new PaperResponse.SimpleRead(
                3L, "회고", "https://example.s3.ap-northeast-2.amazonaws.com/image/default.png", false, true, 56
        );

        PaperResponse.SimpleRead simpleRead4 = new PaperResponse.SimpleRead(
                4L, "전역", "https://example.s3.ap-northeast-2.amazonaws.com/image/default.png", true, false, 105
        );

        given(paperService.readMyRollingPaper(any()))
                .willReturn(
                        List.of(simpleRead1, simpleRead2, simpleRead3, simpleRead4)
                );

        MockHttpServletRequestBuilder httpRequest = RestDocumentationRequestBuilders.get("/paper")
                .header(AUTHORIZATION, "Bearer {token}");

        ResourceSnippetParameters parameters = ResourceSnippetParameters.builder()
                .tag("롤링페이퍼 API")
                .summary("내 롤링페이퍼 조회 API")
                .requestHeaders(
                        headerWithName("Authorization")
                                .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 " +
                                        "또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data[].id").type(NUMBER).description("롤링페이퍼 ID"),
                        fieldWithPath("data[].subject").type(STRING).description("주제"),
                        fieldWithPath("data[].theme").type(STRING).description("테마"),
                        fieldWithPath("data[].isOpen").type(BOOLEAN).description("전체 공개 여부"),
                        fieldWithPath("data[].isLikeOpen").type(BOOLEAN).description("좋아요 공개 여부"),
                        fieldWithPath("data[].likes").type(NUMBER).description("좋아요 수"))
                .build();

        RestDocumentationResultHandler document = documentHandler("readMyRollingPaper", prettyPrint(), parameters);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("롤링페이퍼 수정 API")
    @Test
    void updateRollingPaper() throws Exception {
        // given
        PaperRequest.Update request = new PaperRequest.Update(
                1L, "전역", "default.png", true, true
        );

        MockHttpServletRequestBuilder httpRequest = RestDocumentationRequestBuilders.patch("/paper")
                .header(AUTHORIZATION, "Bearer {token}")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        ResourceSnippetParameters parameters = ResourceSnippetParameters.builder()
                .tag("롤링페이퍼 API")
                .summary("롤링페이퍼 수정 API")
                .requestHeaders(
                        headerWithName("Authorization")
                                .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 " +
                                        "또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .requestFields(
                        fieldWithPath("paperId").type(NUMBER).description("롤링페이퍼 ID"),
                        fieldWithPath("subject").type(STRING).description("주제"),
                        fieldWithPath("theme").type(STRING).description("테마"),
                        fieldWithPath("isOpen").type(BOOLEAN).description("전체 공개 여부"),
                        fieldWithPath("isLikeOpen").type(BOOLEAN).description("전체 공개 여부"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"))
                .requestSchema(Schema.schema("PaperRequest.Update"))
                .responseSchema(Schema.schema("Default"))
                .build();

        RestDocumentationResultHandler document = documentHandler("updateRollingPaper", prettyPrint(), prettyPrint(), parameters);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("롤링페이퍼 삭제 API")
    @Test
    void deleteRollingPaper() throws Exception {
        // given
        MockHttpServletRequestBuilder httpRequest = RestDocumentationRequestBuilders.delete("/paper/{paperId}", 1L)
                .header(AUTHORIZATION, "Bearer {token}");

        ResourceSnippetParameters parameters = ResourceSnippetParameters.builder()
                .tag("롤링페이퍼 API")
                .summary("롤링페이퍼 삭제 API")
                .requestHeaders(
                        headerWithName("Authorization")
                                .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 " +
                                        "또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .pathParameters(
                        parameterWithName("paperId").description("롤링페이퍼 ID"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"))
                .responseSchema(Schema.schema("Default"))
                .build();

        RestDocumentationResultHandler document = documentHandler("deleteRollingPaper", prettyPrint(), parameters);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("롤링페이퍼 정렬 전체 조회 API")
    @Test
    void readAllRollingPaper() throws Exception {
        // given
        PaperResponse.ALL paper1 = PaperResponse.ALL.builder()
                .id(1L)
                .userId(1L)
                .userName("한마디")
                .subject("생일")
                .theme("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                .likes("12")
                .createdAt("2023-08-27")
                .build();

        PaperResponse.ALL paper2 = PaperResponse.ALL.builder()
                .id(4L)
                .userId(12L)
                .userName("고읍동축구왕")
                .subject("한마디 적고가라")
                .theme("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                .likes("96")
                .createdAt("2023-08-13")
                .build();

        PaperResponse.ALL paper3 = PaperResponse.ALL.builder()
                .id(13L)
                .userId(5L)
                .userName("뻐꾸기")
                .subject("인사")
                .theme("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                .likes("527")
                .createdAt("2023-07-21")
                .build();

        given(paperService.readAllRollingPaper(any()))
                .willReturn(List.of(paper1, paper2, paper3));

        MockHttpServletRequestBuilder httpRequest = RestDocumentationRequestBuilders.get("/paper/all")
                .param("sort", "LIKES");

        ResourceSnippetParameters parameters = ResourceSnippetParameters.builder()
                .tag("롤링페이퍼 API")
                .summary("롤링페이퍼 정렬 전체 조회 API")
                .queryParameters(
                        parameterWithName("sort").description("parameter : 'LIKES' or 'LATEST' "))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data[].id").type(NUMBER).description("롤링 페이퍼 ID"),
                        fieldWithPath("data[].userId").type(NUMBER).description("롤링 페이퍼 유저 ID"),
                        fieldWithPath("data[].userName").type(STRING).description("롤링 페이퍼 유저 이름"),
                        fieldWithPath("data[].subject").type(STRING).description("롤링 페이퍼 주제"),
                        fieldWithPath("data[].theme").type(STRING).description("롤링 페이퍼 테마"),
                        fieldWithPath("data[].likes").type(STRING).description("롤링 페이퍼 좋아요 수"),
                        fieldWithPath("data[].createdAt").type(STRING).description("롤링 페이퍼 생성날짜"))
                .responseSchema(Schema.schema("PaperResponse.All"))
                .build();

        RestDocumentationResultHandler document =
                documentHandler("readAllRollingPaper", prettyPrint(), parameters);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("롤링페이퍼 좋아요 및 취소 토글 API")
    @Test
    void toggleLike() throws Exception {
        // given
        given(paperService.toggleLike(anyLong(),any()))
                .willReturn(
                        PaperResponse.ToggleLike.builder()
                                .isAdded(true)
                                .build()
                );

        MockHttpServletRequestBuilder httpRequest = RestDocumentationRequestBuilders.post("/paper/like/{paperId}", 1L)
                .header(AUTHORIZATION, "Bearer {token}");

        ResourceSnippetParameters parameters = ResourceSnippetParameters.builder()
                .tag("롤링페이퍼 API")
                .summary("롤링페이퍼 좋아요 및 취소 토글 API")
                .requestHeaders(
                        headerWithName("Authorization")
                                .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 " +
                                        "또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .pathParameters(
                        parameterWithName("paperId").description("롤링페이퍼 ID"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data.isAdded").type(BOOLEAN).description("좋아요 등록 / 취소 여부"))
                .build();

        RestDocumentationResultHandler document = documentHandler("toggleLike", prettyPrint(), parameters);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }
}
