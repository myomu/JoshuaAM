package site.joshua.am.apicontroller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.joshua.am.domain.User;
import site.joshua.am.security.jwt.constants.JwtConstants;
import site.joshua.am.security.jwt.provider.JwtTokenProvider;
import site.joshua.am.service.RedisRefreshTokenService;
import site.joshua.am.service.UserService;

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
        log.info("cookies: {}", (Object) request.getCookies());

        if (refreshToken == null) {
            return new ResponseEntity<>("Refresh token is missing or expired", HttpStatus.UNAUTHORIZED);
        }

        // Redis 에서 refreshToken 검증
        Long userNo = redisRefreshTokenService.getUserNoByToken(refreshToken);
        if (userNo == null) {
            return new ResponseEntity<>("Refresh token is invalid", HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findUser(userNo);
        if (user == null) return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);

        // 새로운 accessToken 발급
        String newAccessToken = jwtTokenProvider.createToken(userNo, user.getUserLoginId(), user.getAuth(), 1000 * 60);
        // { Authorization : Bearer + {accessToken} } - AccessToken 의 경우 Authorization Header 에 저장한다.
        response.addHeader(JwtConstants.TOKEN_HEADER, JwtConstants.TOKEN_PREFIX + newAccessToken);
        log.info("response.getHeader : {}", response.getHeader(JwtConstants.TOKEN_HEADER));
        log.info("Full Response : {}", response);
        return ResponseEntity.ok().build();
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
}
