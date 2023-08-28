package study.project.backend.domain.paper.Repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import study.project.backend.domain.TestQuerydslConfig;
import study.project.backend.domain.comment.entity.Comment;
import study.project.backend.domain.comment.repository.CommentRepository;
import study.project.backend.domain.paper.entity.Paper;
import study.project.backend.domain.user.entity.Users;
import study.project.backend.domain.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static study.project.backend.domain.user.entity.Authority.ROLE_USER;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestQuerydslConfig.class)
class PaperRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("")
    @Test
    void findByPaperWithFetchJoin() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Users user1 = saveUser("두마디", "twomadee@gmail.com");

        Paper paper = savePaper(user);

        Comment comment1 = saveComment(user1, paper, "생일 축하해 한마디야!");
        Comment comment2 = saveComment(user1, paper, "생일 축하해 선물은 나야!");
        Comment comment3 = saveComment(user1, paper, "생일 축하해 근데 나 배고파");

        entityManager.flush();
        entityManager.clear();

        // when
        Paper validatePaper = paperRepository.findByPaperWithFetchJoin(paper.getId()).get();

        // then
        assertThat(validatePaper)
                .extracting("id", "subject", "theme", "isOpen", "isLikeOpen")
                .contains(paper.getId(), "생일", "default.png", false, false);

        assertThat(validatePaper.getComments())
                .extracting("id", "user.nickName", "content", "imageUrl", "font", "sort", "backgroundColor", "kind")
                .contains(
                        tuple(comment1.getId(), "두마디", "생일 축하해 한마디야!", null, "godic", "center", "white", "친구"),
                        tuple(comment2.getId(), "두마디", "생일 축하해 선물은 나야!", null, "godic", "center", "white", "친구"),
                        tuple(comment3.getId(), "두마디", "생일 축하해 근데 나 배고파", null, "godic", "center", "white", "친구")
                );
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

    private Comment saveComment(Users user1, Paper paper, String content) {
        return commentRepository.save(Comment.builder()
                .user(user1)
                .paper(paper)
                .content(content)
                .font("godic")
                .sort("center")
                .backgroundColor("white")
                .kind("친구")
                .build());
    }

    public Paper savePaper(Users user) {
        return paperRepository.save(
                Paper.builder()
                        .user(user)
                        .subject("생일")
                        .theme("default.png")
                        .isOpen(false)
                        .isLikeOpen(false)
                        .build()
        );
    }
}