package study.project.backend.domain.paper.Repository;

import org.springframework.stereotype.Repository;
import study.project.backend.domain.paper.entity.Paper;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaperCustomRepository {
    Optional<Paper> findByPaperWithFetchJoin(Long paperId);
    List<Paper> findByMyPaperWithFetchJoin(Long userId);
}
