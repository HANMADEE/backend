package study.project.backend.domain.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.project.backend.domain.comment.entity.Comment;

import java.util.List;

import static study.project.backend.domain.comment.entity.QComment.comment;
import static study.project.backend.domain.paper.entity.QPaper.paper;
import static study.project.backend.domain.user.entity.QUsers.users;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findAllByUserWithFetch(Long userId) {
        return queryFactory
                .selectFrom(comment)
                .leftJoin(comment.paper, paper).fetchJoin()
                .leftJoin(paper.user, users).fetchJoin()
                .where(comment.user.id.eq(userId))
                .fetch();
    }
}
