package hu.zmesko.Appointment.security.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {
    private final int MAX_ATTEMPT = 5;
    private final long BLOCK_TIME_MINUTE = 15;

    private final StringRedisTemplate redisTemplate;

    @SuppressWarnings("null")
    public void loginSucceeded(String key) {
        redisTemplate.delete(key);
    }

    @SuppressWarnings("null")
    public void loginFaild(@NonNull String key) {
        String attempts = redisTemplate.opsForValue().get(key);
        int newAttempts = attempts == null ? 1 : Integer.parseInt(attempts) + 1;

        redisTemplate.opsForValue().set(key, Integer.toString(newAttempts));

        if (newAttempts == 1) {
            redisTemplate.expire(key, java.time.Duration.ofMinutes(BLOCK_TIME_MINUTE));
        }
    }

    @SuppressWarnings("null")
    public boolean isBlocked(String key) {
        String attempts = redisTemplate.opsForValue().get(key);
        return attempts != null && Integer.parseInt(attempts) >= MAX_ATTEMPT;
    }
}
