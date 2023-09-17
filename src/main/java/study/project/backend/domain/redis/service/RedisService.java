package study.project.backend.domain.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.project.backend.global.config.redis.RedisDao;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisDao redisDao;

    public Void logoutFromRedis(String email, String accessToken, Long accessTokenExpiration) {
        redisDao.deleteValues(email);
        redisDao.setValues(accessToken, "logout", Duration.ofMillis(accessTokenExpiration));
        return null;
    }
}
