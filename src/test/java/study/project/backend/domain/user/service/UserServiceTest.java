package study.project.backend.domain.user.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import study.project.backend.domain.user.entity.Users;
import study.project.backend.domain.user.repository.UserRepository;
import study.project.backend.domain.user.request.UserRequest;
import study.project.backend.domain.user.request.UserServiceRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static study.project.backend.domain.user.entity.Authority.ROLE_USER;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @DisplayName("유저가 자신의 닉네임을 수정한다.")
    @Test
    void updateNickName() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        UserRequest.UpdateNickName request = new UserRequest.UpdateNickName("두마디");

        em.flush();
        em.clear();

        // when
        userService.updateNickName(request.toServiceRequest(), user.getId());

        // then
        Users validateUser = userRepository.findById(user.getId()).get();
        assertThat(validateUser.getNickName()).isEqualTo("두마디");
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