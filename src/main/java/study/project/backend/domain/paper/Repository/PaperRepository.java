package study.project.backend.domain.paper.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.project.backend.domain.paper.entity.Paper;

import java.util.Optional;

@Repository
public interface PaperRepository extends JpaRepository<Paper, Long> {
    Optional<Paper> findByIdAndUserId(Long id, Long userId);
}
