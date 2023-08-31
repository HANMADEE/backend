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
import study.project.backend.domain.paper.entity.PaperLike;
import study.project.backend.domain.paper.entity.PaperSort;
import study.project.backend.domain.user.entity.Users;
import study.project.backend.domain.user.repository.UserRepository;

import java.util.List;

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
    private PaperLikeRepository paperLikeRepository;

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

        Paper paper = savePaper(user, "생일", false, false);

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

    @DisplayName("롤링페이퍼를 최근순을 입력받아 전체 공개된 롤링페이퍼 리스트를 가져온다.")
    @Test
    void findByAllPaperSortWithFetchJoin() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");

        Paper paper1 = savePaper(user, "생일", true, true);
        Paper paper2 = savePaper(user, "전역", true, false);
        Paper paper3 = savePaper(user, "주제를 뭐로하징", true, false);
        Paper paper4 = savePaper(user, "심심해", true, true);
        Paper paper5 = savePaper(user, "입대", true, false);
        Paper paper6 = savePaper(user, "입사", false, true);
        Paper paper7 = savePaper(user, "생일", true, false);

        // when
        List<Paper> sortPaper =
                paperRepository.findByAllPaperSortWithFetchJoin(PaperSort.LATEST);

        // then
        assertThat(sortPaper)
                .extracting("subject")
                .containsExactly("생일", "입대", "심심해", "주제를 뭐로하징", "전역", "생일");
    }

    @DisplayName("롤링페이퍼를 좋아요순을 입력받아 전체 공개된 롤링페이퍼 리스트를 가져온다.")
    @Test
    void findByAllPaperSortWithFetchJoinForLikes() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Users user2 = saveUser("한마디2", "hanmadee2@gmail.com");
        Users user3 = saveUser("한마디3", "hanmadee3@gmail.com");
        Users user4 = saveUser("한마디4", "hanmadee4@gmail.com");

        Paper paper1 = savePaper(user, "생일", true, true);
        paperLikeRepository.saveAll(List.of(
                toEntityPaperLike(user, paper1),
                toEntityPaperLike(user2, paper1),
                toEntityPaperLike(user3, paper1),
                toEntityPaperLike(user4, paper1)
        ));

        Paper paper2 = savePaper(user, "전역", true, false);
        paperLikeRepository.saveAll(List.of(
                toEntityPaperLike(user, paper2),
                toEntityPaperLike(user4, paper2)
        ));

        Paper paper3 = savePaper(user, "주제를 뭐로하징", true, false);
        paperLikeRepository.saveAll(List.of(
                toEntityPaperLike(user2, paper3)
        ));

        Paper paper4 = savePaper(user, "심심해", true, true);
        paperLikeRepository.saveAll(List.of(
                toEntityPaperLike(user2, paper4),
                toEntityPaperLike(user3, paper4),
                toEntityPaperLike(user4, paper4)
        ));

        Paper paper5 = savePaper(user, "입대", true, false);
        paperLikeRepository.saveAll(List.of(
                toEntityPaperLike(user, paper5),
                toEntityPaperLike(user2, paper5),
                toEntityPaperLike(user3, paper5),
                toEntityPaperLike(user4, paper5)
        ));

        Paper paper6 = savePaper(user, "입사", false, true);

        Paper paper7 = savePaper(user, "생일", true, false);

        // when
        List<Paper> sortPaper =
                paperRepository.findByAllPaperSortWithFetchJoin(PaperSort.LIKES);

        // then
        assertThat(sortPaper)
                .extracting("subject")
                .containsExactly("생일", "입대", "심심해", "전역", "주제를 뭐로하징", "생일");
    }

    private static PaperLike toEntityPaperLike(Users user, Paper paper) {
        return PaperLike.builder()
                .user(user)
                .paper(paper)
                .build();
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

    public Paper savePaper(Users user, String subject, Boolean isOpen, Boolean isLikeOpen) {
        return paperRepository.save(
                Paper.builder()
                        .user(user)
                        .subject(subject)
                        .theme("default.png")
                        .isOpen(isOpen)
                        .isLikeOpen(isLikeOpen)
                        .build()
        );
    }
}