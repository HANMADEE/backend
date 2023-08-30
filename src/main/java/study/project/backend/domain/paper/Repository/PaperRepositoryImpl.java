package study.project.backend.domain.paper.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.project.backend.domain.paper.entity.Paper;
import study.project.backend.domain.paper.entity.QPaperLike;

import java.util.List;
import java.util.Optional;

import static study.project.backend.domain.comment.entity.QComment.comment;
import static study.project.backend.domain.paper.entity.QPaper.paper;
import static study.project.backend.domain.paper.entity.QPaperLike.*;
import static study.project.backend.domain.user.entity.QUsers.users;

@Repository
@RequiredArgsConstructor
public class PaperRepositoryImpl implements PaperCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Paper> findByPaperWithFetchJoin(Long paperId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(paper)
                .leftJoin(paper.comments, comment).fetchJoin()
                .leftJoin(comment.user, users).fetchJoin()
                .where(paper.id.eq(paperId))
                .fetchOne()
        );
    }

    @Override
    public List<Paper> findByMyPaperWithFetchJoin(Long userId) {
        return queryFactory
                .selectFrom(paper)
                .leftJoin(paper.paperLikes, paperLike).fetchJoin()
                .where(paper.user.id.eq(userId))
                .fetch();
    }

}
