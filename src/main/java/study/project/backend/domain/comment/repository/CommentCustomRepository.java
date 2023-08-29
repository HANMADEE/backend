package study.project.backend.domain.comment.repository;

import org.springframework.stereotype.Repository;
import study.project.backend.domain.comment.entity.Comment;

import java.util.List;

@Repository
public interface CommentCustomRepository {
    List<Comment> findAllByUserWithFetch(Long userId);
}
