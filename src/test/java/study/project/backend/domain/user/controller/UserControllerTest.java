package study.project.backend.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import study.project.backend.domain.ControllerTestSupport;
import study.project.backend.domain.user.request.UserRequest;

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
}