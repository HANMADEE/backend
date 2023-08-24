package study.project.backend.domain.user.repository;

import org.springframework.stereotype.Repository;
import study.project.backend.domain.user.entity.Users;

import java.util.List;

@Repository
public interface UserCustomRepository {
    List<Users> findAllByKeyword(String nickName, String email);
}
