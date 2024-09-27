package site.joshua.am.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import site.joshua.am.exception.RedisConnectionException;
import site.joshua.am.security.RefreshToken;
import site.joshua.am.security.jwt.constants.JwtConstants;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisRefreshTokenService {

    private final RedisTemplate<String, RefreshToken> redisTemplate;

    /**
     * 값을 저장
     * @param refreshToken : 리프레시 토큰
     * @param expirationSeconds : 만료 시간
     */
//    public void storeRefreshToken(Long userNo, String refreshToken, long expirationSeconds) {
//        String userKey = "user:" + userNo;
//        String tokenKey = "refreshToken:" + refreshToken;
//
//        // 리프레쉬 토큰을 직렬화 하는 코드 ( 데이터 압축효과도 있음 )
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(refreshToken.getClass()));
//
//        //opsForValue(): RedisTemplate 에서 ValueOperations 를 가져오고
//        //ValueOperations 객체를 통해 Redis 의 값을 저장, 조회, 삭제하는 작업을 수행한다.
//        // 사용자 정보에 refreshToken 저장
//        redisTemplate.opsForValue().set(userKey, refreshToken, expirationSeconds, TimeUnit.SECONDS);
//
//        // refreshToken 에 userNo 저장
//        redisTemplate.opsForValue().set(tokenKey, String.valueOf(userNo), expirationSeconds, TimeUnit.SECONDS);
//    }

    public void storeRefreshToken(RefreshToken refreshToken, long expirationSeconds) {
        String tokenKey = "refreshToken:" + refreshToken.getId();

        // 리프레쉬 토큰을 직렬화 하는 코드 ( 데이터 압축효과도 있음 )
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(refreshToken.getClass()));

        //opsForValue(): RedisTemplate 에서 ValueOperations 를 가져오고
        //ValueOperations 객체를 통해 Redis 의 값을 저장, 조회, 삭제하는 작업을 수행한다.

        // refreshToken 을 Redis 에 저장
        redisTemplate.opsForValue().set(tokenKey, refreshToken, expirationSeconds, TimeUnit.SECONDS);
    }

    /**
     * 키로 값을 조회
     * @param key : username(ID)
     * @return 해당 리프레쉬토큰
     */
    public RefreshToken getRefreshToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // refreshToken 이 있는지 확인하고 요청이 들어온 사용자의 userId, userAgent 가 refreshToken 을 생성할 당시의 값과 같은지 비교한다.
    public boolean validateRefreshToken(String deviceId, String userAgent, String refreshToken) {
        String tokenKey = "refreshToken:" + refreshToken;
        RefreshToken findRefreshToken = getRefreshToken(tokenKey);
        return findRefreshToken != null && findRefreshToken.getDeviceId().equals(deviceId) && findRefreshToken.getUserAgent().equals(userAgent);
    }

    // 새로운 accessToken, refreshToken, deviceId 값을 응답으로 보내는 메서드
    public void setTokenResponse(HttpServletResponse response, String accessToken, String refreshToken, String deviceId) throws IOException {

        // { Authorization : Bearer + {accessToken} } - AccessToken 의 경우 Authorization Header 에 저장한다.
        response.addHeader(JwtConstants.TOKEN_HEADER, JwtConstants.TOKEN_PREFIX + accessToken);
        log.info("response.getHeader : {}", response.getHeader(JwtConstants.TOKEN_HEADER));
        log.info("Full Response : {}", response);

        // 최신 버전의 Spring 에서 지원하는 Cookie 방식을 사용. 기본 Servlet 의 Cookie 에서 지원하지 않는 sameSite 메서드를 지원한다.
        ResponseCookie refreshTokenCookie = ResponseCookie.from(JwtConstants.REFRESH_TOKEN, refreshToken)
                .httpOnly(true) // Http Only 설정
                .secure(true) // HTTPS 에서만 전송
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7일 유효
                .sameSite("None") // SameSite 설정 -> Cloudtype 배포 환경에서 크로스 사이트로 인식이되어 "Lax" 설정을 하면 accessToken 이 삭제되는 문제가 발생하여 "None" 으로 변경함.
                .build();

        ResponseCookie deviceIdCookie = ResponseCookie.from(JwtConstants.DEVICE_ID, deviceId)
                .httpOnly(true) // Http Only 설정
                .secure(true) // HTTPS 에서만 전송
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7일 유효
                .sameSite("None")
                .build();

        // 응답에 쿠키 추가
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        response.addHeader("Set-Cookie", deviceIdCookie.toString());

        //response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * 키로 유저 No 을 조회
     */
//    public Long getUserNoByToken(String refreshToken) {
//        String tokenKey = "refreshToken:" + refreshToken;
//        String userNoStr = redisTemplate.opsForValue().get(tokenKey);
//        return userNoStr != null ? Long.parseLong(userNoStr) : null;
//    }

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

//    public void addToBlackList(String accessToken, String message, Long duration) {
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(message.getClass()));
//        redisTemplate.opsForValue().set(accessToken, message, duration, TimeUnit.MILLISECONDS);
//    }

    public Object getFromBlackList(String accessToken) {
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
