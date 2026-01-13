package hu.zmesko.Appointment.security.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {
    private static final int MAX_ATTEMPT = 5;
    private static final long BLOCK_TIME_MINUTE = 10;
    private static final String LOGIN_ATTEMPT_PREFIX = "auth:loginAttempts:";

    private final StringRedisTemplate redisTemplate;

    public void loginSucceeded(String key) {
        redisTemplate.delete(LOGIN_ATTEMPT_PREFIX + key);
    }

    @SuppressWarnings("null")
    public void loginFailed(String key) {
        Long attempts = redisTemplate.opsForValue().increment(LOGIN_ATTEMPT_PREFIX + key);

        if (attempts == 1) {
            redisTemplate.expire(LOGIN_ATTEMPT_PREFIX + key, java.time.Duration.ofMinutes(BLOCK_TIME_MINUTE));
        }
    }

    public boolean isBlocked(String key) {
        String attempts = redisTemplate.opsForValue().get(LOGIN_ATTEMPT_PREFIX + key);
        return attempts != null && Integer.parseInt(attempts) >= MAX_ATTEMPT;
    }
}
