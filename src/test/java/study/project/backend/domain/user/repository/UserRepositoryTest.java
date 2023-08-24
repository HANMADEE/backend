package study.project.backend.domain.user.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import study.project.backend.domain.TestQuerydslConfig;
import study.project.backend.domain.user.entity.Users;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static study.project.backend.domain.user.entity.Authority.ROLE_USER;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestQuerydslConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저 리스트를 이메일 또는 닉네임으로 검색한다.")
    @Test
    void findAllByKeyword() {
        // given
        Users user1 = toUserEntity("한마디1", "hanmadee1@gmail.com");
        Users user2 = toUserEntity("한마디2", "hanmxxxe2@gmail.com");
        Users user3 = toUserEntity("두마디3", "hanmadee3@gmail.com");
        Users user4 = toUserEntity("두마디4", "hanmxxxe4@gmail.com");
        Users user5 = toUserEntity("한마디5", "hanmxxxe5@gmail.com");
        Users user6 = toUserEntity("세마디6", "hanmadee6@gmail.com");
        Users user7 = toUserEntity("한마디7", "hanmxxxe7@gmail.com");
        Users user8 = toUserEntity("두마디8", "hanmadee8@gmail.com");

        userRepository.saveAll(List.of(user1, user2, user3, user4, user5, user6, user7, user8));

        // when
        List<Users> responseForNickName = userRepository.findAllByKeyword("한마디", null);
        List<Users> responseForEmail = userRepository.findAllByKeyword(null, "hanmxxxe");

        // then
        assertThat(responseForNickName)
                .extracting("nickName", "email")
                .contains(
                        tuple("한마디1", "hanmadee1@gmail.com"),
                        tuple("한마디2", "hanmxxxe2@gmail.com"),
                        tuple("한마디5", "hanmxxxe5@gmail.com"),
                        tuple("한마디7", "hanmxxxe7@gmail.com")
                );

        assertThat(responseForEmail)
                .extracting("nickName", "email")
                .contains(
                        tuple("한마디2", "hanmxxxe2@gmail.com"),
                        tuple("두마디4", "hanmxxxe4@gmail.com"),
                        tuple("한마디5", "hanmxxxe5@gmail.com"),
                        tuple("한마디7", "hanmxxxe7@gmail.com")
                );
    }


    public Users saveUser(String nickName, String email) {
        return userRepository.save(
                toUserEntity(nickName, email)
        );
    }

    private static Users toUserEntity(String nickName, String email) {
        return Users.builder()
                .authority(ROLE_USER)
                .nickName(nickName)
                .email(email)
                .password("password")
                .profileImageUrl("default.png")
                .build();
    }
}