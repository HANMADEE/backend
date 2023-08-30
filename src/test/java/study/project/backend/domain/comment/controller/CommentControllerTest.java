package study.project.backend.domain.comment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import study.project.backend.domain.ControllerTestSupport;
import study.project.backend.domain.comment.request.CommentRequest;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends ControllerTestSupport {

    @DisplayName("한마디 작성 API")
    @Test
    void createComment() throws Exception {
        // given
        CommentRequest.Create request = new CommentRequest.Create(
                "생일축하해 한마디야!", "default.png", "godic", "center", "white", "친구"
        );

        MockHttpServletRequestBuilder httpRequest = MockMvcRequestBuilders.post("/paper/{paperId}/comment", 1L)
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("내 한마디 조회 API")
    @Test
    void readMyComment() throws Exception {
        // given
        MockHttpServletRequestBuilder httpRequest = MockMvcRequestBuilders.get("/comment")
                .header(AUTHORIZATION, "Bearer {token}");

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("한마디 수정 API")
    @Test
    void updateComment() throws Exception {
        // given
        CommentRequest.Update request = new CommentRequest.Update(
                1L, "글수정 테스트", null, "goollim", "left", "white", "친구"
        );

        MockHttpServletRequestBuilder httpRequest = MockMvcRequestBuilders.patch("/comment")
                .header(AUTHORIZATION, "Bearer {token}")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk());
    }
}