package site.joshua.am;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import site.joshua.am.security.jwt.provider.JwtTokenProvider;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public RateLimitConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public LoginRateLimitingFilter loginRateLimitingFilter() {
        return new LoginRateLimitingFilter();
    }

    @Bean
    public RateLimitInterceptor rateLimitInterceptor() {
        return new RateLimitInterceptor(userRateLimiter(), this);
    }

    @Bean
    public UserRateLimiter userRateLimiter() {
        return new UserRateLimiter();
    }

    // 1. 로그인 Rate Limiting 필터
    public static class LoginRateLimitingFilter extends OncePerRequestFilter {

        private final Bucket loginBucket = Bucket.builder()
                .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1))))
                .build();

        @Override
        protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
                throws ServletException, IOException {
            if ("/api/login".equals(request.getRequestURI())) {
                if (!loginBucket.tryConsume(1)) {
                    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                    return;
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    // 2. 사용자별 Rate Limiting 관리
    public static class UserRateLimiter {
        private final ConcurrentHashMap<String, Bucket> userBuckets = new ConcurrentHashMap<>();

        public Bucket resolveBucket(String userId) {
            return userBuckets.computeIfAbsent(userId, key -> Bucket.builder()
                    .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofSeconds(1))))
                    .build());
        }
    }

    // JWT에서 사용자 ID를 추출하는 메서드
    public String extractUsernameFromToken(String token) {
        Jws<Claims> parsedToken = jwtTokenProvider.parseToken(token);
        return parsedToken.getPayload().get("username").toString();
    }

    // 3. 글로벌 Rate Limiting 인터셉터
    @Component
    public static class RateLimitInterceptor implements HandlerInterceptor {

        private final Bucket globalBucket = Bucket.builder()
                .addLimit(Bandwidth.classic(100, Refill.intervally(100, Duration.ofSeconds(1))))
                .build();

        private final UserRateLimiter userRateLimiter;
        private final RateLimitConfig rateLimitConfig;

        public RateLimitInterceptor(UserRateLimiter userRateLimiter, RateLimitConfig rateLimitConfig) {
            this.userRateLimiter = userRateLimiter;
            this.rateLimitConfig = rateLimitConfig;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
            // JWT 토큰에서 사용자 ID 추출
            String authHeader = request.getHeader("Authorization");
            String username = "guest";
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 이후의 토큰
                username = rateLimitConfig.extractUsernameFromToken(token); // 사용자 ID 추출
            }

            // 사용자별 Rate Limiting 적용
            Bucket userBucket = userRateLimiter.resolveBucket(username);
            if (!userBucket.tryConsume(1)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                return false;
            }

            // 글로벌 Rate Limiting 적용
            if (!globalBucket.tryConsume(1)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                return false;
            }

            return true;
        }
    }
}


