package study.project.backend.domain.paper.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import study.project.backend.domain.paper.request.PaperRequest;
import study.project.backend.domain.paper.response.PaperResponse;
import study.project.backend.domain.user.entity.Users;
import study.project.backend.domain.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static study.project.backend.domain.user.entity.Authority.ROLE_USER;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PaperServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaperService paperService;

    @DisplayName("유저가 테마를 선택하지 않고 롤링페이퍼를 만든다.")
    @Test
    void createRollingPaperWithNotChoiceTheme() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        PaperRequest.Create request = new PaperRequest.Create("생일", null, false, false);

        // when
        PaperResponse.Create response = paperService.createRollingPaper(request.toServiceRequest(), user.getId());

        // then
        assertThat(response)
                .extracting("userId", "subject", "theme", "isOpen", "isLikeOpen")
                .contains(user.getId(), "생일", "default.png", false, false);
    }

    @DisplayName("유저가 테마를 선택하지 않고 롤링페이퍼를 만든다.")
    @Test
    void createRollingPaperWithChoiceTheme() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        PaperRequest.Create request = new PaperRequest.Create("생일", "겨울왕국", false, false);

        // when
        PaperResponse.Create response = paperService.createRollingPaper(request.toServiceRequest(), user.getId());

        // then
        assertThat(response)
                .extracting("userId", "subject", "theme", "isOpen", "isLikeOpen")
                .contains(user.getId(), "생일", "겨울왕국", false, false);
    }

    public Users saveUser(String nickName, String email) {
        return userRepository.save(
                Users.builder()
                        .authority(ROLE_USER)
                        .nickName(nickName)
                        .email(email)
                        .password("password")
                        .profileImageUrl("default.png")
                        .build()
        );
    }
}