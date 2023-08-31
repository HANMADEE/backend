package study.project.backend.domain.paper.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.project.backend.domain.paper.entity.Paper;
import study.project.backend.domain.paper.entity.PaperSort;
import study.project.backend.global.common.Result;
import study.project.backend.global.common.exception.CustomException;

import java.util.List;
import java.util.Optional;

import static study.project.backend.domain.comment.entity.QComment.comment;
import static study.project.backend.domain.paper.entity.QPaper.paper;
import static study.project.backend.domain.paper.entity.QPaperLike.paperLike;
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

    @Override
    public List<Paper> findByAllPaperSortWithFetchJoin(PaperSort sort) {
        return queryFactory
                .selectFrom(paper)
                .leftJoin(paper.paperLikes, paperLike).fetchJoin()
                .leftJoin(paper.user, users).fetchJoin()
                .where(paper.isOpen.eq(true))
                .orderBy(sortBy(sort))
                .fetch();
    }

    private OrderSpecifier<?> sortBy(PaperSort sort) {
        switch (sort.name()) {
            case "LIKES" -> {
                return paper.paperLikes.size().desc();
            }
            case "LATEST" -> {
                return paper.createdAt.desc();
            }
        }

        throw new CustomException(Result.UNSUPPORTED_SORT_OPTION);
    }


}
