package site.joshua.am.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import site.joshua.am.domain.CustomUser;
import site.joshua.am.domain.UserAuth;
import site.joshua.am.security.jwt.constants.JwtConstants;
import site.joshua.am.security.jwt.provider.JwtTokenProvider;
import site.joshua.am.service.RedisRefreshTokenService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

    private final ObjectMapper objectMapper = new ObjectMapper();
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

        // 사용자 인증 (로그인)
        authentication = authenticationManager.authenticate(authentication);

        log.info("인증 여부 : {}", authentication.isAuthenticated());

        // 인증 실패 (username, password 불일치)
        if(!authentication.isAuthenticated()) {
            log.info("인증 실패 : 아이디 또는 비밀번호가 일치하지 않습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 UNAUTHORIZED (인증 실패)
        }

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
        Long userNo = user.getUser().getId();
        String userId = user.getUser().getUserLoginId();
        UserAuth role = user.getUser().getAuth();

        // AccessToken 생성
        long accessTokenExpirationTime = 1000 * 10; // 1000 * 60 * 15; // 15분
        String accessToken = jwtTokenProvider.createToken(userNo, userId, role, accessTokenExpirationTime);

        // Create RefreshToken
        String refreshToken = UUID.randomUUID().toString();

        // RefreshToken 을 Redis 에 저장
        String refreshTokenKey = "refreshToken:" + userNo;
        int refreshTokenExpirationDays = 7; // 7일 유효
        redisRefreshTokenService.storeRefreshToken(userNo, refreshToken, refreshTokenExpirationDays * 24 * 60 * 60); // 초 단위

        // 사용자 정보 캐싱 (사용자 ID를 키로 사용자 이름 및 역할 저장) ? 필요한가? - 캐싱은 후 순위로 일단 두고 보자.

        setTokenResponse(response, accessToken, refreshToken);

    }



    private void setTokenResponse(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {

        // { Authorization : Bearer + {accessToken} } - AccessToken 의 경우 Authorization Header 에 저장한다.
        response.addHeader(JwtConstants.TOKEN_HEADER, JwtConstants.TOKEN_PREFIX + accessToken);
        log.info("response.getHeader : {}", response.getHeader(JwtConstants.TOKEN_HEADER));
        log.info("Full Response : {}", response.toString());

        // 최신 버전의 Spring 에서 지원하는 Cookie 방식을 사용. 기본 Servlet 의 Cookie 에서 지원하지 않는 sameSite 메서드를 지원한다.
        ResponseCookie refreshTokenCookie = ResponseCookie.from(JwtConstants.REFRESH_TOKEN, refreshToken)
                .httpOnly(true) // Http Only 설정
                .secure(true) // HTTPS 에서만 전송
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7일 유효
                .sameSite("Lax") // SameSite 설정
                .build();

        // 응답에 쿠키 추가
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        //response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        // 토큰을 JSON 응답으로 보냄
//        Map<String, Object> tokens = new HashMap<>();
//        tokens.put("accessToken", accessToken);
//        tokens.put("refreshToken", refreshToken);
//
//        new ObjectMapper().writeValue(response.getOutputStream(), tokens);

        // AccessToken 을 HttpOnly 쿠키로 설정
        //response.addCookie(createHttpOnlyCookie("accessToken", accessToken));

        // RefreshToken 을 HttpOnly 쿠키로 설정
        //Cookie refreshTokenCookie = new Cookie(JwtConstants.REFRESH_TOKEN, refreshToken);
//        refreshTokenCookie.setHttpOnly(true); // Http Only 설정
//        refreshTokenCookie.setSecure(true); // HTTPS 에서만 전송
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(24 * 60 * 60); // 1일 유효
//        refreshTokenCookie.
        //response.addCookie(createHttpOnlyCookie(JwtConstants.REFRESH_TOKEN, refreshToken));


    }

    private Cookie createHttpOnlyCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 쿠키 만료 시간 - 1일
        return cookie;
    }
}
