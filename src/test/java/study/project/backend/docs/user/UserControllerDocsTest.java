package study.project.backend.docs.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import study.project.backend.docs.RestDocsSupport;
import study.project.backend.domain.user.controller.Platform;
import study.project.backend.domain.user.controller.UserController;
import study.project.backend.domain.user.response.UserResponse;
import study.project.backend.domain.user.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.formParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerDocsTest extends RestDocsSupport {

    private final UserService userService = mock(UserService.class);

    @Override
    protected Object initController() {
        return new UserController(userService);
    }

    @DisplayName("소셜로그인 API")
    @Test
    void socialLogin() throws Exception {
        // given
        given(userService.socialLogin(anyString(), any(Platform.class)))
                .willReturn(UserResponse.Login.builder()
                        .userId(1L)
                        .email("hanmadee@gmail.com")
                        .nickName("한마디")
                        .profileImageUrl("default.png")
                        .accessToken("token")
                        .refreshToken("token")
                        .build()
                );

        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/signin")
                                .param("code", "JKWHNF2CA78acSW6AUw7cvxWsxzaAWVNKR34SAA0AZ")
                                .param("platform", "KAKAO")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("socialLogin",
                        preprocessResponse(prettyPrint()),
                        formParameters(
                                parameterWithName("code").description("발급받은 인가코드"),
                                parameterWithName("platform").description("플랫폼 : 'GOOGLE' / 'KAKAO' ")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(STRING).description("상태 메세지"),
                                fieldWithPath("data.userId").type(NUMBER).description("유저 ID"),
                                fieldWithPath("data.email").type(STRING).description("유저 이메일"),
                                fieldWithPath("data.nickName").type(STRING).description("유저 닉네임"),
                                fieldWithPath("data.profileImageUrl").type(STRING).description("유저 프로필 이미지"),
                                fieldWithPath("data.accessToken").type(STRING).description("액세스 토큰"),
                                fieldWithPath("data.refreshToken").type(STRING).description("리프레쉬 토큰")
                        )
                ));
    }
}
