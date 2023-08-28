package study.project.backend.domain.paper.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import study.project.backend.domain.ControllerTestSupport;
import study.project.backend.domain.paper.request.PaperRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaperControllerTest extends ControllerTestSupport {

    @DisplayName("롤링페이퍼 만들기 API")
    @Test
    void createRollingPaper() throws Exception {
        // given
        PaperRequest.Create request = new PaperRequest.Create("생일", null, false, false);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/paper")
                .header("Authorization", "Bearer Token")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        // when // then
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("롤링페이퍼 만들기 API - 공개 여부 미선택 실패")
    @Test
    void createRollingPaperWithNotInsertedIsOpenThrowException() throws Exception {
        // given
        PaperRequest.Create request = new PaperRequest.Create("생일", null, null, false);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/paper")
                .header("Authorization", "Bearer Token")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON);

        // when // then
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("롤링페이퍼 선물하기 API")
    @Test
    void giftRollingPaper() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/paper/gift")
                .header("Authorization", "Bearer Token")
                .param("paperId", "1")
                .param("giftedUserId", "3");

        // when // then
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("롤링페이퍼 조회 API")
    @Test
    void readRollingPaper() throws Exception {
        // given
        MockHttpServletRequestBuilder httpRequest = MockMvcRequestBuilders.get("/paper/{paperId}", 1L);

        // when // then
        mockMvc.perform(httpRequest)
                .andDo(print())
                .andExpect(status().isOk());

    }
}