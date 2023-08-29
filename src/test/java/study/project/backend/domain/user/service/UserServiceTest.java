package study.project.backend.domain.user.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import study.project.backend.domain.user.entity.Users;
import study.project.backend.domain.user.repository.UserRepository;
import study.project.backend.domain.user.request.UserRequest;
import study.project.backend.domain.user.response.UserResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @DisplayName("유저가 서비스에 로그인을 진행한다.")
    @Test
    void login() {
        // given
        Users user = saveUser("흥해라흥", "1000test@naver.com", "Abc12345!");
        UserRequest.Login request = new UserRequest.Login("1000test@naver.com", "Abc12345!");

        // when
        UserResponse.Login response = userService.login(request.toServiceRequest());

        // then
        assertThat(response)
                .extracting("userId", "email", "nickName", "profileImageUrl")
                .contains(user.getId(), "1000test@naver.com", "default.png");

        assertThat(response.getAccessToken()).isNotNull();
        assertThat(response.getRefreshToken()).isNotNull();
    }

    @DisplayName("유저가 서비스에 로그인을 진행할때 비밀번호가 맞지 않는다.")
    @Test
    void loginWithNotMatchedPassword() {
        // given
        Users user = saveUser("흥해라흥", "1000test@naver.com", "Abc12346!");
        UserRequest.Login request = new UserRequest.Login("1000test@naver.com", "Abc12345!");

        // when // then
        assertThatThrownBy(() -> userService.login(request.toServiceRequest()))
                .extracting("result.code", "result.message")
                .contains(-1002, "비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("유저가 서비스에 로그인을 진행할때 소셜로그인 진행한다.")
    @Test
    void loginWithNotSocialEmail() {
        // given
        Users user = saveUser("흥해라흥", "1000test@gmail.com", passwordEncoder.encode("GOOGLE"));
        UserRequest.Login request = new UserRequest.Login("1000test@gmail.com", "GOOGLE");

        // when // then
        assertThatThrownBy(() -> userService.login(request.toServiceRequest()))
                .extracting("result.code", "result.message")
                .contains(-1003, "해당 아이디는 소셜로그인으로 회원가입된 회원입니다.");
    }

    @DisplayName("유저가 서비스의 회원가입을 진행한다.")
    @Test
    void register() {
        // given
        UserRequest.Register request = new UserRequest.Register(
                "흥해라한마디",
                "1000test@naver.com",
                "Abc12345!",
                null
        );

        // when
        UserResponse.Register response = userService.register(request.toServiceRequest());

        // then
        assertThat(response)
                .extracting("nickName", "email", "profileImageUrl")
                .contains("흥해라한마디", "1000test@naver.com", "default.png");
    }

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

    @DisplayName("사용자가 닉네임, 또는 이메일로 서비스의 유저들을 검색한다.")
    @Test
    void searchUsers() {
        // given
        Users user1 = toUserEntity("한마디1", "hanmadee1@gmail.com");
        Users user2 = toUserEntity("한마디2", "hanmxxxe2@gmail.com");
        Users user3 = toUserEntity("두마디3", "hanmadee3@gmail.com");
        Users user4 = toUserEntity("두마디4", "hanmxxxe4@gmail.com");
        Users user5 = toUserEntity("한마디5", "hanmxxxe5@gmail.com");
        Users user6 = toUserEntity("세마디6", "hanmadee6@gmail.com");
        Users user7 = toUserEntity("한마디7", "hanmxxxe7@gmail.com");
        Users user8 = toUserEntity("두마디8", "hanmadee8@gmail.com");

        List<Users> users =
                userRepository.saveAll(List.of(user1, user2, user3, user4, user5, user6, user7, user8));

        UserRequest.Search request = new UserRequest.Search(null, "hanmxxxe");

        // when
        List<UserResponse.Search> response = userService.searchUsers(request.toServiceRequest());

        // then
        assertThat(response)
                .extracting("userId", "email", "nickName")
                .contains(
                        tuple(users.get(1).getId(), users.get(1).getEmail(), users.get(1).getNickName()),
                        tuple(users.get(3).getId(), users.get(3).getEmail(), users.get(3).getNickName()),
                        tuple(users.get(4).getId(), users.get(4).getEmail(), users.get(4).getNickName()),
                        tuple(users.get(6).getId(), users.get(6).getEmail(), users.get(6).getNickName())
                );
    }

    @DisplayName("사용자가 닉네임, 또는 이메일로 서비스의 유저들을 검색한다.")
    @Test
    void searchUsersWithNotNickNameAndEmailIsNullThrowException() {
        // given
        UserRequest.Search request = new UserRequest.Search(null, null);

        // when // then
        assertThatThrownBy(() -> userService.searchUsers(request.toServiceRequest()))
                .extracting("result.code", "result.message", "debug")
                .contains(
                        -1001,
                        "닉네임과 이메일 중 하나는 NULL이 아니어야 합니다.",
                        "닉네임과 이메일 중 하나는 NULL이 아니어야 합니다."
                );
    }

    @DisplayName("유저가 자신의 프로필 정보를 조회한다.")
    @Test
    void readUser() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        em.flush();
        em.clear();

        // when
        UserResponse.Search response = userService.readUser(user.getId());

        // then
        assertThat(response)
                .extracting("userId", "email", "nickName")
                .contains(user.getId(), "hanmadee@gmail.com", "한마디");
    }

    public Users saveUser(String nickName, String email) {
        return userRepository.save(
                toUserEntity(nickName, email)
        );
    }

    public Users saveUser(String nickName, String email, String password) {
        return userRepository.save(
                toUserEntity(nickName, email, password)
        );
    }

    private Users toUserEntity(String nickName, String email) {
        return Users.builder()
                .authority(ROLE_USER)
                .nickName(nickName)
                .email(email)
                .password("password")
                .profileImageUrl("default.png")
                .build();
    }

    private Users toUserEntity(String nickName, String email, String password) {
        return Users.builder()
                .authority(ROLE_USER)
                .nickName(nickName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .profileImageUrl("default.png")
                .build();
    }

}