package study.project.backend.domain.paper.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import study.project.backend.domain.comment.entity.Comment;
import study.project.backend.domain.comment.repository.CommentRepository;
import study.project.backend.domain.paper.Repository.PaperLikeRepository;
import study.project.backend.domain.paper.Repository.PaperRepository;
import study.project.backend.domain.paper.entity.Paper;
import study.project.backend.domain.paper.entity.PaperLike;
import study.project.backend.domain.paper.entity.PaperSort;
import study.project.backend.domain.paper.request.PaperRequest;
import study.project.backend.domain.paper.response.PaperResponse;
import study.project.backend.domain.user.entity.Users;
import study.project.backend.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static study.project.backend.domain.user.entity.Authority.ROLE_USER;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PaperServiceTest {

    @Autowired
    private PaperLikeRepository paperLikeRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PaperRepository paperRepository;

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

    @DisplayName("유저가 다른 유저에게 롤링페이퍼를 선물한다.")
    @Test
    void giftRollingPaper() {
        // given
        Users user1 = saveUser("한마디1", "hanmadee1@gmail.com");
        Users user2 = saveUser("한마디2", "hanmadee2@gmail.com");
        Paper paper = savePaper(user1);

        // when
        PaperResponse.Create response =
                paperService.giftRollingPaper(paper.getId(), user2.getId(), user1.getId());

        // then
        assertThat(response)
                .extracting("userId", "subject", "theme", "isOpen", "isLikeOpen")
                .contains(user2.getId(), "생일", "default.png", false, false);
    }

    @DisplayName("유저가 자신의 롤링페이퍼를 조회한다.")
    @Test
    void readRollingPaper() {
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
        PaperResponse.Read response = paperService.readRollingPaper(paper.getId());

        // then
        assertThat(response)
                .extracting("id", "userId", "subject", "theme", "isOpen", "isLikeOpen")
                .contains(paper.getId(), user.getId(), "생일", "default.png", false, false);

        assertThat(response.getComments())
                .extracting("id", "userName", "content", "imageUrl", "font", "sort", "backgroundColor", "kind")
                .contains(
                        tuple(comment1.getId(), "두마디", "생일 축하해 한마디야!", null, "godic", "center", "white", "친구"),
                        tuple(comment2.getId(), "두마디", "생일 축하해 선물은 나야!", null, "godic", "center", "white", "친구"),
                        tuple(comment3.getId(), "두마디", "생일 축하해 근데 나 배고파", null, "godic", "center", "white", "친구")
                );
    }

    @DisplayName("유저가 자신의 롤링페이퍼를 조회할때 익명의 사용자도 포함되어있다.")
    @Test
    void readRollingPaperWithAnonymous() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Users user1 = saveUser("두마디", "twomadee@gmail.com");

        Paper paper = savePaper(user);

        Comment comment1 = saveComment(user1, paper, "생일 축하해 한마디야!");
        Comment comment2 = saveComment(null, paper, "생일 축하해 선물은 나야!");
        Comment comment3 = saveComment(null, paper, "생일 축하해 근데 나 배고파");

        entityManager.flush();
        entityManager.clear();

        // when
        PaperResponse.Read response = paperService.readRollingPaper(paper.getId());

        // then
        assertThat(response)
                .extracting("id", "userId", "subject", "theme", "isOpen", "isLikeOpen")
                .contains(paper.getId(), user.getId(), "생일", "default.png", false, false);

        assertThat(response.getComments())
                .extracting("id", "userName", "content", "imageUrl", "font", "sort", "backgroundColor", "kind")
                .contains(
                        tuple(comment1.getId(), "두마디", "생일 축하해 한마디야!", null, "godic", "center", "white", "친구"),
                        tuple(comment2.getId(), "익명", "생일 축하해 선물은 나야!", null, "godic", "center", "white", "친구"),
                        tuple(comment3.getId(), "익명", "생일 축하해 근데 나 배고파", null, "godic", "center", "white", "친구")
                );
    }

    @DisplayName("유저가 자신의 롤링페이퍼들을 조회한다.")
    @Test
    void readMyRollingPaper() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Users user1 = saveUser("두마디", "doomadee@gmail.com");

        Paper paper1 = savePaper(user);
        Paper paper2 = savePaper(user);
        Paper paper3 = savePaper(user);

        PaperLike paperLike1 = savePaperLike(user1, paper1);
        PaperLike paperLike2 = savePaperLike(user1, paper3);

        entityManager.flush();
        entityManager.clear();

        // when
        List<PaperResponse.SimpleRead> response = paperService.readMyRollingPaper(user.getId());

        // then
        assertThat(response)
                .extracting("id", "likes")
                .contains(
                        tuple(paper1.getId(), 1),
                        tuple(paper2.getId(), 0),
                        tuple(paper3.getId(), 1)
                );
    }

    @DisplayName("유저가 자신의 롤링페이퍼를 수정한다.")
    @Test
    void updateRollingPaper() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Paper paper = savePaper(user);

        PaperRequest.Update request = new PaperRequest.Update(
                paper.getId(), "전역", "default.png", true, true
        );

        entityManager.flush();
        entityManager.clear();

        // when
        paperService.updateRollingPaper(request.toServiceRequest(), user.getId());

        // then
        Paper validatePaper = paperRepository.findById(paper.getId()).get();
        assertThat(validatePaper)
                .extracting("subject", "theme", "isOpen", "isLikeOpen")
                .contains("전역", "default.png", true, true);
    }

    @DisplayName("유저가 다른 사람의 롤링페이퍼를 수정할때 Exception 발생한다.")
    @Test
    void updateRollingPaperWithNotMyPaperThrowException() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Users user1 = saveUser("한마디", "hanmadee@gmail.com");
        Paper paper = savePaper(user);

        PaperRequest.Update request = new PaperRequest.Update(
                paper.getId(), "전역", "default.png", true, true
        );

        entityManager.flush();
        entityManager.clear();

        // when // then
        assertThatThrownBy(
                () -> paperService.updateRollingPaper(request.toServiceRequest(), user1.getId()))
                .extracting("result.code", "result.message")
                .contains(-2001, "내가 만든 롤링페이퍼가 아닙니다.");
    }

    @DisplayName("유저가 자신의 롤링페이퍼를 삭제한다.")
    @Test
    void deleteRollingPaper() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Paper paper = savePaper(user);

        entityManager.flush();
        entityManager.clear();

        Long paperId = paper.getId();

        // when
        paperService.deleteRollingPaper(paperId, user.getId());

        // then
        Optional<Paper> optionalPaper = paperRepository.findById(paperId);
        assertThat(optionalPaper.isEmpty()).isTrue();
    }

    @DisplayName("유저가 다른 사람의 롤링페이퍼를 삭제할때 Exception 발생한다.")
    @Test
    void deleteRollingPaperWithNotMyPaperThrowException() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Users user1 = saveUser("한마디", "hanmadee@gmail.com");
        Paper paper = savePaper(user);

        entityManager.flush();
        entityManager.clear();

        Long paperId = paper.getId();

        // when // then
        assertThatThrownBy(() -> paperService.deleteRollingPaper(paperId, user1.getId()))
                .extracting("result.code","result.message")
                .contains(-2001, "내가 만든 롤링페이퍼가 아닙니다.");
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

        Paper paper2 = savePaper(user3, "전역", true, false);
        paperLikeRepository.saveAll(List.of(
                toEntityPaperLike(user, paper2),
                toEntityPaperLike(user4, paper2)
        ));

        Paper paper3 = savePaper(user, "주제를 뭐로하징", true, false);
        paperLikeRepository.saveAll(List.of(
                toEntityPaperLike(user2, paper3)
        ));

        Paper paper4 = savePaper(user2, "심심해", true, true);
        paperLikeRepository.saveAll(List.of(
                toEntityPaperLike(user2, paper4),
                toEntityPaperLike(user3, paper4),
                toEntityPaperLike(user4, paper4)
        ));

        Paper paper5 = savePaper(user2, "입대", true, false);
        paperLikeRepository.saveAll(List.of(
                toEntityPaperLike(user, paper5),
                toEntityPaperLike(user2, paper5),
                toEntityPaperLike(user3, paper5),
                toEntityPaperLike(user4, paper5)
        ));

        Paper paper6 = savePaper(user, "입사", false, true);

        Paper paper7 = savePaper(user4, "생일", true, false);

        // when
        List<PaperResponse.ALL> response =
                paperService.readAllRollingPaper(PaperSort.LIKES);

        // then
        assertThat(response)
                .extracting("userName","subject")
                .containsExactly(
                        tuple("한마디", "생일"),
                        tuple("한마디2", "입대"),
                        tuple("한마디2", "심심해"),
                        tuple("한마디3", "전역"),
                        tuple("한마디", "주제를 뭐로하징"),
                        tuple("한마디4", "생일"));
    }

    @DisplayName("유저가 롤링페이퍼에 좋아요를 남긴다.")
    @Test
    void toggleLikeWithAdd() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Paper paper = savePaper(user);

        // when
        PaperResponse.ToggleLike response = paperService.toggleLike(paper.getId(), user.getId());

        // then
        assertThat(response.getIsAdded()).isTrue();
    }

    @DisplayName("유저가 롤링페이퍼에 좋아요를 취소한다")
    @Test
    void toggleLikeWithCancel() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Paper paper = savePaper(user);
        PaperLike paperLike = savePaperLike(user, paper);

        // when
        PaperResponse.ToggleLike response = paperService.toggleLike(paper.getId(), user.getId());

        // then
        assertThat(response.getIsAdded()).isFalse();
    }

    private PaperLike savePaperLike(Users user, Paper paper) {
        return paperLikeRepository.save(
                PaperLike.builder()
                        .user(user)
                        .paper(paper)
                        .build()
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

    private static PaperLike toEntityPaperLike(Users user, Paper paper) {
        return PaperLike.builder()
                .user(user)
                .paper(paper)
                .build();
    }
}