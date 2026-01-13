package hu.zmesko.Appointment.security.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import hu.zmesko.Appointment.security.util.Sha256Hasher;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final String REFRESH_TOKEN_PREFIX = "auth:refresh:";

    private final StringRedisTemplate redisTemplate;
    private final JwtService jwtService;
    private final Sha256Hasher sha256Hasher;

    @SuppressWarnings("null")
    public void putRefreshTokenInRedis(String token) {
        String username = jwtService.extractUsername(token);
        long expirationTime = Math.max(jwtService.extractExpiration(token).getTime() - System.currentTimeMillis(), 0);
        String hashedToken = sha256Hasher.hash(token);

        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + hashedToken, username, expirationTime,
                TimeUnit.MILLISECONDS);
    }

    public String rotateRefreshToken(String currentToken) {
        String hashedCurrentTokenKey = REFRESH_TOKEN_PREFIX + sha256Hasher.hash(currentToken);

        String username = redisTemplate.opsForValue().get(hashedCurrentTokenKey);
        if (username == null) {
            throw new IllegalStateException("Refresh token not found in Redis");
        }

        Long ttl = redisTemplate.getExpire(hashedCurrentTokenKey, TimeUnit.MILLISECONDS);
        if (ttl == null || ttl <= 0) {
            throw new IllegalStateException("Refresh token expired");
        }

        String newToken = jwtService.generateRefreshToken(username, ttl);
        String hashedNewTokenKey = REFRESH_TOKEN_PREFIX + sha256Hasher.hash(newToken);
        redisTemplate.delete(hashedCurrentTokenKey);
        redisTemplate.opsForValue().set(hashedNewTokenKey, username, ttl, TimeUnit.MILLISECONDS);

        return newToken;
    }

    public void deleteRefreshToken(String refreshToken) {
        String hashedTokenKey = REFRESH_TOKEN_PREFIX + sha256Hasher.hash(refreshToken);

        redisTemplate.delete(hashedTokenKey);
    }

    public boolean isTokenInRedis(String token) {
        String hashedToken = sha256Hasher.hash(token);
        String key = REFRESH_TOKEN_PREFIX + hashedToken;

        return redisTemplate.opsForValue().get(key) != null;
    }
}
