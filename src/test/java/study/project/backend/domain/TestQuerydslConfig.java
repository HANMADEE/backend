package study.project.backend.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import study.project.backend.domain.user.repository.UserRepositoryImpl;

@TestConfiguration
@RequiredArgsConstructor
public class TestQuerydslConfig {

    private final EntityManager entityManager;

    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public UserRepositoryImpl userRepositoryImpl() {
        return new UserRepositoryImpl(queryFactory());
    }
}
