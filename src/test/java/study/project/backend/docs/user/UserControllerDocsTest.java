package study.project.backend.docs.user;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
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

    @DisplayName("회원가입 API")
    @Test
    void register() throws Exception {
        // given
        given(userService.register(any(UserServiceRequest.Register.class)))
                .willReturn(UserResponse.Register.builder()
                        .id(1L)
                        .nickName("흥해라한마디")
                        .email("1000test@naver.com")
                        .profileImageUrl("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                        .build()
                );

        UserRequest.Register request = new UserRequest.Register(
                "흥해라한마디",
                "1000test@naver.com",
                "Abc12345!",
                null
        );

        MockHttpServletRequestBuilder httpRequest = RestDocumentationRequestBuilders.post("/auth/register")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("유저 API")
                                .summary("회원가입 API")
                                .requestFields(
                                        fieldWithPath("nickName").type(STRING).description("닉네임"),
                                        fieldWithPath("email").type(STRING).description("이메일"),
                                        fieldWithPath("password").type(STRING).description("비밀번호 : 대문자,소문자,특수문자 숫자 포함 & 8자 이상 20자 이하"),
                                        fieldWithPath("profileImageUrl").type(STRING).description("optional : 프로필 이미지").optional())
                                .responseFields(
                                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                                        fieldWithPath("data.id").type(NUMBER).description("유저 ID"),
                                        fieldWithPath("data.nickName").type(STRING).description("유저 닉네임"),
                                        fieldWithPath("data.email").type(STRING).description("유저 이메일"),
                                        fieldWithPath("data.profileImageUrl").type(STRING).description("유저 프로필 이미지"))
                                .build())));
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
                                .tag("유저 API")
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
                                .header(AUTHORIZATION, "Bearer {token}")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("updateNickName",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("유저 API")
                                .summary("닉네임 수정 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 " +
                                                        "또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                                .requestFields(
                                        fieldWithPath("nickName").type(STRING).description("유저 닉네임"))
                                .responseFields(
                                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                                        fieldWithPath("message").type(STRING).description("상태 메세지"))
                                .requestSchema(Schema.schema("UserRequest.UpdateNickName"))
                                .responseSchema(Schema.schema("Default"))
                                .build())));
    }

    @DisplayName("내 정보 조회 API")
    @Test
    void readUser() throws Exception {
        // given
        given(userService.readUser(any()))
                .willReturn(UserResponse.Search.builder()
                        .userId(1L)
                        .email("hanmadee@gmail.com")
                        .nickName("한마디")
                        .profileImageUrl("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                        .build());

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/auth").header(AUTHORIZATION, "Bearer {token}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("readUser",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("유저 API")
                                .summary("내 정보 조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 " +
                                                        "또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                                .responseFields(
                                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                                        fieldWithPath("data.userId").type(NUMBER).description("유저 ID"),
                                        fieldWithPath("data.email").type(STRING).description("유저 이메일"),
                                        fieldWithPath("data.nickName").type(STRING).description("유저 닉네임"),
                                        fieldWithPath("data.profileImageUrl").type(STRING).description("유저 프로필 이미지"))
                                .responseSchema(Schema.schema("UserResponse.Search"))
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
                                .tag("유저 API")
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

    @DisplayName("로그인 API")
    @Test
    void login() throws Exception {
        // given
        UserRequest.Login request = new UserRequest.Login("1000test@naver.com","Abc12345!");

        given(userService.login(any()))
                .willReturn(
                        UserResponse.Login.builder()
                                .userId(1L)
                                .email("1000test@naver.com")
                                .nickName("야채쿵야")
                                .profileImageUrl("https://example.s3.ap-northeast-2.amazonaws.com/image/default.png")
                                .accessToken("access token")
                                .refreshToken("refresh token")
                                .build()
                );

        MockHttpServletRequestBuilder httpRequest = RestDocumentationRequestBuilders.post("/auth/login")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("유저 API")
                                .summary("로그인 API")
                                .requestFields(
                                        fieldWithPath("email").type(STRING).description("이메일"),
                                        fieldWithPath("password").type(STRING).description("비밀번호"))
                                .responseFields(
                                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                                        fieldWithPath("data.userId").type(NUMBER).description("유저 ID"),
                                        fieldWithPath("data.email").type(STRING).description("유저 이메일"),
                                        fieldWithPath("data.nickName").type(STRING).description("유저 닉네임"),
                                        fieldWithPath("data.profileImageUrl").type(STRING).description("유저 프로필 사진"),
                                        fieldWithPath("data.accessToken").type(STRING).description("발급된 액세스 토큰"),
                                        fieldWithPath("data.refreshToken").type(STRING).description("발급된 리프레쉬 토큰"))
                                .requestSchema(Schema.schema("UserRequest.Login"))
                                .responseSchema(Schema.schema("UserResponse.Login"))
                                .build())));
    }

    @DisplayName("내 정보 수정 API")
    @Test
    void updateUser() throws Exception {
        // given
        UserRequest.Update request = new UserRequest.Update("한마디", "1000test@naver.com");

        MockHttpServletRequestBuilder httpRequest = RestDocumentationRequestBuilders.patch("/auth")
                .header(AUTHORIZATION, "Bearer {token}")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("updateUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("유저 API")
                                .summary("내 정보 수정 API")
                                .requestFields(
                                        fieldWithPath("email").type(STRING).description("변경할 이메일"),
                                        fieldWithPath("nickName").type(STRING).description("변경할 닉네임"))
                                .responseFields(
                                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                                        fieldWithPath("message").type(STRING).description("상태 메세지"))
                                .requestSchema(Schema.schema("UserRequest.Update"))
                                .responseSchema(Schema.schema("UserResponse.Update"))
                                .build())));
    }

    @DisplayName("비밀번호 변경 API")
    @Test
    void updatePassword() throws Exception{
        // given
        UserRequest.UpdatePassword request = new UserRequest.UpdatePassword(
                "Abc1234!",
                "Abc12345!",
                "Abc12344!"
        );

        MockHttpServletRequestBuilder httpRequest = RestDocumentationRequestBuilders.patch("/auth/password")
                .header(AUTHORIZATION, "Bearer {token}")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        ResourceSnippetParameters parameters = ResourceSnippetParameters.builder()
                .tag("유저 API")
                .summary("비밀번호 변경 API")
                .requestHeaders(
                        headerWithName("Authorization")
                                .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 " +
                                        "또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .requestFields(
                        fieldWithPath("oldPassword").type(STRING).description("이전 비밀번호"),
                        fieldWithPath("newPassword").type(STRING).description("새로운 비밀번호"),
                        fieldWithPath("checkNewPassword").type(STRING).description("새로운 비밀번호 확인"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"))
                .requestSchema(Schema.schema("UserRequest.UpdatePassword"))
                .responseSchema(Schema.schema("Default"))
                .build();

        RestDocumentationResultHandler document =
                documentHandler("updatePassword", prettyPrint(), prettyPrint(), parameters);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }
}
