package study.project.backend.domain.paper.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/paper")
                        .header("Authorization", "token")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

}