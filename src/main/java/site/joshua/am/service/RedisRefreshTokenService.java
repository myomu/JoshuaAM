package site.joshua.am.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import site.joshua.am.exception.RedisConnectionException;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisRefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 값을 저장
     * @param userNo : userNO(DB의 USER ID)
     * @param refreshToken : 리프레시 토큰
     * @param expirationSeconds : 만료 시간
     */
    public void storeRefreshToken(Long userNo, String refreshToken, long expirationSeconds) {
        String userKey = "user:" + userNo;
        String tokenKey = "refreshToken:" + refreshToken;

        // 리프레쉬 토큰을 직렬화 하는 코드 ( 데이터 압축효과도 있음 )
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(refreshToken.getClass()));

        //opsForValue(): RedisTemplate 에서 ValueOperations 를 가져오고
        //ValueOperations 객체를 통해 Redis 의 값을 저장, 조회, 삭제하는 작업을 수행한다.
        // 사용자 정보에 refreshToken 저장
        redisTemplate.opsForValue().set(userKey, refreshToken, expirationSeconds, TimeUnit.SECONDS);

        // refreshToken 에 userNo 저장
        redisTemplate.opsForValue().set(tokenKey, String.valueOf(userNo), expirationSeconds, TimeUnit.SECONDS);
    }

    /**
     * 키로 값을 조회
     * @param key : username(ID)
     * @return 해당 리프레쉬토큰
     */
    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 키로 유저 No 을 조회
     */
    public Long getUserNoByToken(String refreshToken) {
        String tokenKey = "refreshToken:" + refreshToken;
        String userNoStr = redisTemplate.opsForValue().get(tokenKey);
        return userNoStr != null ? Long.parseLong(userNoStr) : null;
    }

    /**
     *키로 값을 삭제
     * @param key : username(ID)
     */
    public void removeRefreshToken(String key) {
        redisTemplate.delete(key);
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // RefreshToken BlackList

    public void addToBlackList(String accessToken, String message, Long duration) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(message.getClass()));
        redisTemplate.opsForValue().set(accessToken, message, duration, TimeUnit.MILLISECONDS);
    }

    public String getFromBlackList(String accessToken) {
        return redisTemplate.opsForValue().get(accessToken);
    }

    public boolean deleteFromBlackList(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.delete(accessToken));
    }

    /**
     * Redis All data delete
     */
    public void flushAll() {
        try {
            Objects.requireNonNull(redisTemplate.getConnectionFactory(), "Redis Connection Factory is null")
                    .getConnection()
                    .serverCommands()
                    .flushAll();
            log.info("Successfully flushed all keys from Redis.");
        } catch (NullPointerException e) {
            log.error("Failed to flush all keys: Redis Connection Factory is null.", e);
            throw new RedisConnectionException("Redis Connection Factory is null", e);
        } catch (Exception e) {
            log.error("An error occurred while flushing all keys from Redis.", e);
            throw new RedisConnectionException("An error occurred while flushing all keys from Redis.", e);
        }
    }


}
