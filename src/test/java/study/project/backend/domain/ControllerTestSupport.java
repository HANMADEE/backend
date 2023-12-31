package study.project.backend.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import study.project.backend.VersionController;
import study.project.backend.domain.comment.controller.CommentController;
import study.project.backend.domain.paper.controller.PaperController;
import study.project.backend.domain.report.controller.ReportController;
import study.project.backend.domain.user.controller.UserController;

@WebMvcTest(controllers = {
        VersionController.class,
        UserController.class,
        PaperController.class,
        CommentController.class,
        ReportController.class
})
@AutoConfigureMockMvc(addFilters = false)
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected UserController userController;

    @MockBean
    protected VersionController versionController;

    @MockBean
    protected PaperController paperController;

    @MockBean
    protected CommentController controller;

    @MockBean
    protected ReportController reportController;
}
