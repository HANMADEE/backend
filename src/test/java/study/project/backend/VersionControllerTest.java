package study.project.backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class VersionControllerTest {

    @Autowired
    private VersionController versionController;

    @DisplayName("서버의 버전을 확인한다.")
    @Test
    void versionCheck() {
        // given
        // when
        String version = versionController.serverVersionCheck();

        // then
        assertThat(version).isEqualTo("Version 0.0.2");
    }
}