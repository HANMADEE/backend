package study.project.backend.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.project.backend.domain.user.entity.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
}
