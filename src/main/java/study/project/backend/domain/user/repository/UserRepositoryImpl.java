package study.project.backend.domain.user.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.project.backend.domain.user.entity.Users;

import java.util.List;

import static study.project.backend.domain.user.entity.QUsers.users;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Users> findAllByKeyword(String nickName, String email) {
        return queryFactory
                .selectFrom(users)
                .where(
                        findContainsNickname(nickName),
                        findContainsEmail(email))
                .fetch();
    }

    // method
    private static BooleanExpression findContainsNickname(String nickName) {
        if (!StringUtils.hasText(nickName)) {
            return null;
        }
        return users.nickName.contains(nickName);
    }

    private static BooleanExpression findContainsEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }
        return users.email.contains(email);
    }
}
