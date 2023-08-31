package study.project.backend.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.project.backend.domain.comment.entity.Comment;
import study.project.backend.domain.report.entity.Report;
import study.project.backend.domain.user.entity.Users;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByUserIdAndCommentId(Long userId, Long commentId);
}
