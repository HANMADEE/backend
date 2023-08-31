package study.project.backend.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import study.project.backend.domain.ControllerTestSupport;
import study.project.backend.domain.user.request.UserRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTestSupport {

    @DisplayName("소셜로그인 API")
    @Test
    void socialLogin() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/signin")
                                .param("code", "JKWHNF2CA78acSW6AUw7cvxWsxzaAWVNKR34SAA0AZ")
                                .param("platform", "KAKAO")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("닉네임 수정 API")
    @Test
    void updateNickName() throws Exception {
        // given
        UserRequest.UpdateNickName request = new UserRequest.UpdateNickName("한마디");

        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/auth/signin")
                                .header("Authorization", "Bearer Token")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("유저 검색 API")
    @Test
    void searchUsers() throws Exception {
        // given
        UserRequest.Search request = new UserRequest.Search(null, "hanmadee");

        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/auth/users")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("내 정보 조회 API")
    @Test
    void readUser() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/auth")
                                .header(AUTHORIZATION, "Bearer {token}")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입 API")
    @Test
    void register() throws Exception {
        // given
        UserRequest.Register request = new UserRequest.Register(
                "흥해라한마디",
                "1000test@naver.com",
                "Abc12345!",
                null
        );

        MockHttpServletRequestBuilder httpRequest = MockMvcRequestBuilders.post("/auth/register")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입 API - 비밀번호 형식 실패")
    @Test
    void registerWithNotPatternPasswordThrowException() throws Exception {
        // given
        UserRequest.Register request = new UserRequest.Register(
                "흥해라한마디",
                "1000test@naver.com",
                "abc12345",
                null
        );

        MockHttpServletRequestBuilder httpRequest = MockMvcRequestBuilders.post("/auth/register")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 API - 이메일 형식 실패")
    @Test
    void registerWithNotPatternEmailThrowException() throws Exception {
        // given
        UserRequest.Register request = new UserRequest.Register(
                "흥해라한마디",
                "1000test-naver.com",
                "Abc12345!",
                null
        );

        MockHttpServletRequestBuilder httpRequest = MockMvcRequestBuilders.post("/auth/register")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 API - 비밀번호 사이즈 실패")
    @Test
    void registerWithNotSizePasswordThrowException() throws Exception {
        // given
        UserRequest.Register request = new UserRequest.Register(
                "흥해라한마디",
                "1000test@naver.com",
                "abc1234",
                null
        );

        MockHttpServletRequestBuilder httpRequest = MockMvcRequestBuilders.post("/auth/register")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("로그인 API")
    @Test
    void login() throws Exception {
        // given
        UserRequest.Login request = new UserRequest.Login("1000test@naver.com", "Abc12345!");

        MockHttpServletRequestBuilder httpRequest = MockMvcRequestBuilders.post("/auth/login")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        // when // then

        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("내 정보 수정 API")
    @Test
    void updateUser() throws Exception {
        // given
        UserRequest.Update request = new UserRequest.Update("한마디", "1000test@naver.com");

        MockHttpServletRequestBuilder httpRequest = MockMvcRequestBuilders.patch("/auth")
                .header(AUTHORIZATION, "Bearer {token}")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk());
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

        MockHttpServletRequestBuilder httpRequest = MockMvcRequestBuilders.patch("/auth/password")
                .header(AUTHORIZATION, "Bearer {token}")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk());
    }
}