package study.project.backend.docs.user;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import study.project.backend.docs.RestDocsSupport;
import study.project.backend.domain.user.controller.Platform;
import study.project.backend.domain.user.controller.UserController;
import study.project.backend.domain.user.request.UserRequest;
import study.project.backend.domain.user.request.UserServiceRequest;
import study.project.backend.domain.user.response.UserResponse;
import study.project.backend.domain.user.service.UserService;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerDocsTest extends RestDocsSupport {

    private final UserService userService = mock(UserService.class);

    @Override
    protected Object initController() {
        return new UserController(userService);
    }

    @DisplayName("소셜 로그인 API")
    @Test
    void socialLogin() throws Exception {
        // given
        given(userService.socialLogin(anyString(), any(Platform.class)))
                .willReturn(UserResponse.Login.builder()
                        .userId(1L)
                        .email("hanmadee@gmail.com")
                        .nickName("한마디")
                        .profileImageUrl("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                        .accessToken("token")
                        .refreshToken("token")
                        .build()
                );

        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/auth/signin")
                                .param("code", "JKWHNF2CA78acSW6AUw7cvxWsxzaAWVNKR34SAA0AZ")
                                .param("platform", "KAKAO")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("socialLogin",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("User API")
                                .summary("소셜 로그인 API")
                                .formParameters(
                                        parameterWithName("code").description("발급받은 인가코드"),
                                        parameterWithName("platform").description("플랫폼 : 'GOOGLE' / 'KAKAO' "))
                                .responseFields(
                                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                                        fieldWithPath("data.userId").type(NUMBER).description("유저 ID"),
                                        fieldWithPath("data.email").type(STRING).description("유저 이메일"),
                                        fieldWithPath("data.nickName").type(STRING).description("유저 닉네임"),
                                        fieldWithPath("data.profileImageUrl").type(STRING).description("유저 프로필 이미지"),
                                        fieldWithPath("data.accessToken").type(STRING).description("액세스 토큰"),
                                        fieldWithPath("data.refreshToken").type(STRING).description("리프레쉬 토큰"))
                                .requestSchema(Schema.schema("FormParameter-socialLogin"))
                                .responseSchema(Schema.schema("UserResponse.Login"))
                                .build())));
    }

    @DisplayName("닉네임 수정 API")
    @Test
    void updateNickName() throws Exception {
        // given
        UserRequest.UpdateNickName request = new UserRequest.UpdateNickName("한마디");

        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.patch("/auth/signin")
                                .header("Authorization", "Bearer Token")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("updateNickName",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("User API")
                                .summary("닉네임 수정 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("Bearer Token"))
                                .requestFields(
                                        fieldWithPath("nickName").type(STRING).description("유저 닉네임"))
                                .responseFields(
                                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                                        fieldWithPath("message").type(STRING).description("상태 메세지"))
                                .requestSchema(Schema.schema("UserRequest.UpdateNickName"))
                                .responseSchema(Schema.schema("Default Response"))
                                .build())));
    }

    @DisplayName("유저 검색 API")
    @Test
    void searchUsers() throws Exception {
        // given
        UserRequest.Search request = new UserRequest.Search(null, "hanmadee");

        UserResponse.Search user1 = UserResponse.Search.builder()
                .userId(1L)
                .email("hanmadee@gmail.com")
                .nickName("한마디")
                .profileImageUrl("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                .build();

        UserResponse.Search user2 = UserResponse.Search.builder()
                .userId(2L)
                .email("1000peach@gmail.com")
                .nickName("복숭아")
                .profileImageUrl("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                .build();

        UserResponse.Search user3 = UserResponse.Search.builder()
                .userId(3L)
                .email("moidots@gmail.com")
                .nickName("모이닷")
                .profileImageUrl("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                .build();

        given(userService.searchUsers(any(UserServiceRequest.Search.class)))
                .willReturn(List.of(user1, user2, user3));

        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/auth/users")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("searchUsers",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("User API")
                                .summary("유저 검색 API")
                                .requestFields(
                                        fieldWithPath("nickName").type(STRING).description("유저 닉네임").optional(),
                                        fieldWithPath("email").type(STRING).description("검색 이메일 / 아이디도 가능").optional()
                                )
                                .responseFields(
                                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                                        fieldWithPath("data[].userId").type(NUMBER).description("유저 ID"),
                                        fieldWithPath("data[].email").type(STRING).description("유저 이메일"),
                                        fieldWithPath("data[].nickName").type(STRING).description("유저 닉네임"),
                                        fieldWithPath("data[].profileImageUrl").type(STRING).description("유저 프로필 이미지"))
                                .requestSchema(Schema.schema("UserRequest.Search"))
                                .responseSchema(Schema.schema("UserResponse.Search"))
                                .build())));
    }
}
