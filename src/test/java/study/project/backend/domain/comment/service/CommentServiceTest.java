package study.project.backend.domain.comment.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import study.project.backend.domain.comment.entity.Comment;
import study.project.backend.domain.comment.repository.CommentRepository;
import study.project.backend.domain.comment.request.CommentRequest;
import study.project.backend.domain.comment.response.CommentResponse;
import study.project.backend.domain.paper.Repository.PaperRepository;
import study.project.backend.domain.paper.entity.Paper;
import study.project.backend.domain.user.entity.Users;
import study.project.backend.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static study.project.backend.domain.user.entity.Authority.ROLE_USER;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("익명의 사용자가 롤링페이퍼에 코멘트를 남긴다.")
    @Test
    void createComment() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Paper paper = savePaper(user);

        CommentRequest.Create request = new CommentRequest.Create(
                "생일축하해 한마디야!", "default.png", "godic", "center", "white", "지인"
        );

        // when
        CommentResponse.Create response =
                commentService.createComment(paper.getId(), request.toServiceRequest(), Optional.empty());

        // then
        assertThat(response)
                .extracting(
                        "paperId", "userId", "userName",
                        "content", "imageUrl", "font",
                        "sort", "backgroundColor", "kind")
                .contains(
                        paper.getId(), 0L, "익명",
                        "생일축하해 한마디야!", "default.png", "godic",
                        "center", "white", "지인");
    }

    @DisplayName("서비스 유저가 롤링페이퍼에 코멘트를 남긴다.")
    @Test
    void createCommentWithLoginUser() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Users user1 = saveUser("김상식", "kimedu@gmail.com");
        Paper paper = savePaper(user);

        CommentRequest.Create request = new CommentRequest.Create(
                "생일축하해 한마디야!", "default.png", "godic", "center", "white", "친구"
        );

        // when
        CommentResponse.Create response =
                commentService.createComment(paper.getId(), request.toServiceRequest(), Optional.ofNullable(user1.getId()));

        // then
        assertThat(response)
                .extracting(
                        "paperId", "userId", "userName",
                        "content", "imageUrl", "font",
                        "sort", "backgroundColor", "kind")
                .contains(
                        paper.getId(), user1.getId(), user1.getNickName(),
                        "생일축하해 한마디야!", "default.png", "godic",
                        "center", "white", "친구");
    }

    @DisplayName("유저가 자신이 남긴 한마디들을 조회한다.")
    @Test
    void readMyComment() {
        // given
        Users user = saveUser("한마딩", "1000test@naver.com");

        Users user1 = saveUser("두마딩", "1000testing@naver.com");

        Paper paper = savePaper(user);

        saveComment(user1, paper, "무엇을 적을까요1");
        saveComment(user1, paper, "무엇을 적을까요2");
        saveComment(user1, paper, "무엇을 적을까요3");

        entityManager.flush();
        entityManager.clear();

        // when
        List<CommentResponse.Read> response = commentService.readMyComment(user1.getId());

        // then
        assertThat(response)
                .extracting("paperUserName", "content")
                .contains(
                        tuple("한마딩", "무엇을 적을까요1"),
                        tuple("한마딩", "무엇을 적을까요2"),
                        tuple("한마딩", "무엇을 적을까요3")
                );

    }

    @DisplayName("유저가 자신이 남긴 코멘트를 수정한다.")
    @Test
    void updateComment() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Users user2 = saveUser("한마디2", "hanmadee2@gmail.com");
        Paper paper = savePaper(user);
        Comment comment = saveComment(user2, paper, "생일축하해!");

        Long commentId = comment.getId();
        CommentRequest.Update request = new CommentRequest.Update(
                commentId, "글수정 테스트", null, "goollim", "left", "white", "친구"
        );

        entityManager.flush();
        entityManager.clear();

        // when
        commentService.updateComment(request.toServiceRequest(), user2.getId());

        // then
        Comment validateComment = commentRepository.findById(commentId).get();
        assertThat(validateComment)
                .extracting("content", "imageUrl", "font", "sort", "backgroundColor", "kind")
                .contains("글수정 테스트", null, "goollim", "left", "white", "친구");
    }

    @DisplayName("유저가 다른 사람이 남긴 코멘트를 수정하려하면 Exception 발생한다.")
    @Test
    void updateCommentWithNotMyCommentThrowException() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Users user2 = saveUser("한마디2", "hanmadee2@gmail.com");
        Paper paper = savePaper(user);
        Comment comment = saveComment(user2, paper, "생일축하해!");

        Long commentId = comment.getId();
        CommentRequest.Update request = new CommentRequest.Update(
                commentId, "글수정 테스트", null, "goollim", "left", "white", "친구"
        );

        entityManager.flush();
        entityManager.clear();

        // when // then
        assertThatThrownBy(() -> commentService.updateComment(request.toServiceRequest(), user.getId()))
                .extracting("result.code", "result.message")
                .contains(-3001, "내가 작성한 코멘트가 아닙니다.");

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

    private Comment saveComment(Users user, Paper paper, String content) {
        return commentRepository.save(Comment.builder()
                .user(user)
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