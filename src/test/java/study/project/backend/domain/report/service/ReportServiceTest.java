package study.project.backend.domain.report.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import study.project.backend.domain.comment.entity.Comment;
import study.project.backend.domain.comment.repository.CommentRepository;
import study.project.backend.domain.paper.Repository.PaperRepository;
import study.project.backend.domain.paper.entity.Paper;
import study.project.backend.domain.report.entity.Report;
import study.project.backend.domain.report.repository.ReportRepository;
import study.project.backend.domain.report.request.ReportRequest;
import study.project.backend.domain.user.entity.Users;
import study.project.backend.domain.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static study.project.backend.domain.user.entity.Authority.ROLE_USER;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    @DisplayName("유저가 다른 유저의 코멘트를 신고한다.")
    @Test
    void reportedComment() {
        // given
        Users user = saveUser("한마디", "hanmadee@gmail.com");
        Users user2 = saveUser("한마디2", "hanmadee2@gmail.com");
        Paper paper = savePaper(user);
        Comment comment = saveComment(user2, paper, "너 노잼");

        ReportRequest.Report request = new ReportRequest.Report(
                user2.getId(), comment.getId(), "저보고 재미없다는데요"
        );

        // when
        reportService.reportUser(request.toServiceRequest(), user.getId());

        // then
        Optional<Report> optionalReport =
                reportRepository.findByUserIdAndCommentId(user.getId(), comment.getId());

        assertThat(optionalReport.isPresent()).isTrue();
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