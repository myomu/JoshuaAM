package site.joshua.am.security.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import site.joshua.am.security.jwt.constants.JwtConstants;
import site.joshua.am.security.jwt.provider.JwtTokenProvider;

import java.io.IOException;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;


    // 생성자
    public JwtRequestFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * JWT 요청 필터 (Filter 중 첫번째 filter)
     * - request > headers > Authorization (JWT)
     * - JWT 토큰 유효성 검사
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // request 요청 method, url, corsAllow 확인
        log.info("Method : {}", request.getMethod());
        log.info("RequestURL : {}", request.getRequestURL().toString());
        log.info("Header-Origin : {}", request.getHeader("Origin"));

        // refresh-token, login 경로는 필터링하지 않음
        if (request.getRequestURI().equals("/api/auth/refresh-token") || request.getRequestURI().startsWith("/api/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 JWT 토큰을 가져옴
        String header = request.getHeader(JwtConstants.TOKEN_HEADER);
        log.info("Authorization: {}", header);

        Cookie[] cookies = request.getCookies();
        String deviceId = "";
        String refreshToken = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("deviceId")) {
                    deviceId = cookie.getValue();
                }
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    log.info("refreshToken: {}", cookie.getValue());
                }
            }
        }

        // JWT 토큰이 없으면 다음 필터로 이동 -> JwtAuthenticationFilter : Bearer + {jwt}
        if (header == null || !header.startsWith(JwtConstants.TOKEN_PREFIX)) {
            //filterChain.doFilter(request, response); <- /login 때문에 doFilter 를 해주었지면 위에서 처리해주었으므로 여기선 에러를 보내준다.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Token is missing");
            return;
        }

        // AccessToken : Bearer + {jwt} -> "Bearer " 제거
        String accessToken = header.replace(JwtConstants.TOKEN_PREFIX, "");

        // 토큰 해석
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken, refreshToken);
//        if (authentication == null) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Authentication Failed");
//            return;
//        }

        // 토큰 유효성 검사
        if (jwtTokenProvider.validateToken(accessToken, deviceId)) {
            log.info("유효한 JWT 토큰입니다.");

            // 로그인
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.info("Invalid Token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT Token is expired or Invalid Token");
            return;
        }

        // 다음 필터
        filterChain.doFilter(request, response);
    }

}
