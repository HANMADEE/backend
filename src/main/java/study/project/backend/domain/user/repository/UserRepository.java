package study.project.backend.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.project.backend.domain.user.entity.Users;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>, UserCustomRepository {
    Optional<Users> findByEmail(String email);
}
