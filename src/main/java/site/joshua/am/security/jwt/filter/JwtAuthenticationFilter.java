package site.joshua.am.security.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import site.joshua.am.domain.CustomUser;
import site.joshua.am.domain.UserAuth;
import site.joshua.am.security.RefreshToken;
import site.joshua.am.security.jwt.constants.JwtConstants;
import site.joshua.am.security.jwt.provider.JwtTokenProvider;
import site.joshua.am.service.RedisRefreshTokenService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *          (/login)
 * client -> filter -> server
 * ✅ username, password 인증 시도 (attemptAuthentication)
 *      ❌ 인증 실패 : response > status : 401 (UNAUTHORIZED)
 *
 *      ⭕ 인증 성공 (successfulAuthentication)
 *          -> JWT 생성
 *          -> response > headers > authorization : (JWT)
 */
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisRefreshTokenService redisRefreshTokenService;

    // 생성자 - Security Config 에 등록된 빈을 의존성 주입으로 사용할 수 없어서 생성자에 넣어서 사용하도록 한다
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, RedisRefreshTokenService redisRefreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisRefreshTokenService = redisRefreshTokenService;
        // 필터 URL 경로 설정 : /login
        setFilterProcessesUrl(JwtConstants.AUTH_LOGIN_URL);
    }

    /**
     * 인증 시도 메서드
     * : /login 경로로 요청하면, 필터로 걸러서 인증을 시도
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        log.info("username: {}", username);
        log.info("password: {}", password);

        // 사용자 인증정보 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        try {
            // 사용자 인증 (로그인)
            authentication = authenticationManager.authenticate(authentication);
        } catch (AuthenticationException e) {
            log.error("인증 실패 : 아이디 또는 비밀번호가 일치하지 않습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 UNAUTHORIZED (인증 실패)
            try {
                response.getWriter().write("Invalid username or password");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }

        log.info("인증 여부 : {}", authentication.isAuthenticated());

        return authentication; // successfulAuthentication 에 파라미터로 으로 넘어간다.
    }

    /**
     * 인증 성공 메서드
     *
     * - JWT 를 생성
     * - JWT 를 응답 헤더에 설정
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException, ServletException {

        log.info("인증 성공...");

        CustomUser user = (CustomUser) authentication.getPrincipal();
        Long userId = user.getUser().getId();
        String username = user.getUser().getUserLoginId();
        UserAuth role = user.getUser().getAuth();

        String userAgent = request.getHeader("User-Agent");
        String deviceId = UUID.randomUUID().toString();

        // AccessToken 생성
        long accessTokenExpirationTime = 1000 * 10; // 1000 * 60 * 15; // 15분
        String accessToken = jwtTokenProvider.createToken(deviceId, username, role, accessTokenExpirationTime);

        // Create RefreshToken, RefreshToken 을 Redis 에 저장
        RefreshToken newRefreshToken = new RefreshToken();
        int refreshTokenExpirationDays = 7; // 7일 유효
        newRefreshToken.generateRefreshToken(userId, username, role.getDescription(), userAgent, deviceId, LocalDateTime.now().plusDays(refreshTokenExpirationDays));
        redisRefreshTokenService.storeRefreshToken(newRefreshToken, refreshTokenExpirationDays * 24 * 60 * 60);

        // 사용자 정보 캐싱 (사용자 ID를 키로 사용자 이름 및 역할 저장) ? 필요한가? - 캐싱은 후 순위로 일단 두고 보자.

        // 토큰을 쿠키로 만들어 응답을 내보낸다.
        redisRefreshTokenService.setTokenResponse(response, accessToken, newRefreshToken.getId(), newRefreshToken.getDeviceId());
    }





}
