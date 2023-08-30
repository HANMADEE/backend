package study.project.backend.domain.paper.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.project.backend.domain.paper.entity.PaperLike;

@Repository
public interface PaperLikeRepository extends JpaRepository<PaperLike, Long> {
}
