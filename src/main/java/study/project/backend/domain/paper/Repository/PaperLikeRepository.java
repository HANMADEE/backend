package study.project.backend.domain.paper.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.project.backend.domain.paper.entity.Paper;
import study.project.backend.domain.paper.entity.PaperLike;
import study.project.backend.domain.user.entity.Users;

import java.util.Optional;

@Repository
public interface PaperLikeRepository extends JpaRepository<PaperLike, Long> {
    Optional<PaperLike> findByPaperAndUser(Paper paper, Users user);
}
