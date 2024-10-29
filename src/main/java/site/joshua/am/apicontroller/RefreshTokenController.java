package site.joshua.am.apicontroller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.joshua.am.domain.User;
import site.joshua.am.domain.UserAuth;
import site.joshua.am.security.RefreshToken;
import site.joshua.am.security.jwt.provider.JwtTokenProvider;
import site.joshua.am.service.RedisRefreshTokenService;
import site.joshua.am.service.UserService;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenController {


    private final RedisRefreshTokenService redisRefreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    /**
     * Refresh Token 을 통한 Access Token 재발급
     */
    @PostMapping("/auth/refresh-token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 쿠키에서 refreshToken 가져오기
        String refreshToken = getRefreshTokenFromCookies(request);
        log.info("refreshToken: {}", refreshToken);

        String deviceId = getDeviceIdFromCookies(request);
        log.info("deviceId: {}", deviceId);

        if (refreshToken == null) {
            return new ResponseEntity<>("Refresh token is missing or expired", HttpStatus.UNAUTHORIZED);
        }

        RefreshToken findRefreshToken = redisRefreshTokenService.getRefreshToken("refreshToken:" + refreshToken);
        Long userId = findRefreshToken.getUserId();
        String userAgent = request.getHeader("User-Agent");

        // RefreshToken 검증(userId, userAgent,
        if (redisRefreshTokenService.validateRefreshToken(deviceId, userAgent, refreshToken)) {
            User user = userService.findUser(userId);
            if (user == null) return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);

            String username = user.getUserLoginId();
            UserAuth role = user.getAuth();

            // 새로운 accessToken 생성
            String newDeviceId = UUID.randomUUID().toString(); // 새로운 deviceId
            long accessTokenExpirationTime = 1000 * 60 * 5; //* 60 * 5; // 5분
            String newAccessToken = jwtTokenProvider.createToken(newDeviceId, user.getUserLoginId(), user.getAuth(), accessTokenExpirationTime);

            // 새로운 refreshToken 생성
            RefreshToken newRefreshToken = new RefreshToken();
            int refreshTokenExpirationDays = 7; // 7일 유효
            newRefreshToken.generateRefreshToken(userId, username, role.getDescription(), userAgent, newDeviceId, LocalDateTime.now().plusDays(refreshTokenExpirationDays));
            redisRefreshTokenService.storeRefreshToken(newRefreshToken, refreshTokenExpirationDays * 24 * 60 * 60);

            // 기존 refreshToken 을 Redis 에서 제거
            redisRefreshTokenService.removeRefreshToken("refreshToken:" + refreshToken);

            redisRefreshTokenService.setTokenResponse(response, newAccessToken, newRefreshToken.getId(), newDeviceId);

            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@CookieValue String refreshToken) {
        if (refreshToken != null) {
            // Redis 에서 refresh Token 삭제
            redisRefreshTokenService.removeRefreshToken(refreshToken);

            // 쿠키 삭제 (로그아웃 처리 후)
            ResponseCookie deleteRefreshToken = ResponseCookie.from("refreshToken", "")
                    .path("/")
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .maxAge(0) // 쿠키 만료
                    .build();

            ResponseCookie deleteDeviceId = ResponseCookie.from("deviceId", "")
                    .path("/")
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .maxAge(0) // 쿠키 만료
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, deleteRefreshToken.toString())
                    .header(HttpHeaders.SET_COOKIE, deleteDeviceId.toString())
                    .body("Logout Success");
        } else {
            return ResponseEntity.badRequest().body("Invalid Request");
        }
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String getDeviceIdFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("deviceId")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
