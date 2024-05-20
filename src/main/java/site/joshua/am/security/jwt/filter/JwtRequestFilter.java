package site.joshua.am.security.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
     * JWT 요청 필터
     * - request > headers > Authorization (JWT)
     * - JWT 토큰 유효성 검사
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // request 요청 method, url 확인
        log.info("Method : {}", request.getMethod());
        log.info("RequestURI : {}", request.getRequestURI());

        // 헤더에서 JWT 토큰을 가져옴
        String header = request.getHeader(JwtConstants.TOKEN_HEADER);
        log.info("authorization {}", header);

        // JWT 토큰이 없으면 다음 필터로 이동
        // Bearer + {jwt}
        if (header == null || !header.startsWith(JwtConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT
        // Bearer + {jwt} -> "Bearer " 제거
        String jwt = header.replace(JwtConstants.TOKEN_PREFIX, "");

        // 토큰 해석
        Authentication authentication = jwtTokenProvider.getAuthentication(jwt);

        // 토큰 유효성 검사
        if (jwtTokenProvider.validateToken(jwt)) {
            log.info("유효한 JWT 토큰입니다.");

            // 로그인
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터 - 추가 확장시
        filterChain.doFilter(request, response);
    }

}
