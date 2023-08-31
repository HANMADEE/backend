package study.project.backend.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VersionControllerTest extends ControllerTestSupport {

    @DisplayName("서버의 버전을 확인한다.")
    @Test
    void versionCheck() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/version")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}