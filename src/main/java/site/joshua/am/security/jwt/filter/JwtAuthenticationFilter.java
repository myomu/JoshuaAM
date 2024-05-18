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
import site.joshua.am.security.jwt.constants.JwtConstants;
import site.joshua.am.security.jwt.provider.JwtTokenProvider;

import java.io.IOException;

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

    // 생성자 - Security Config 에 등록된 빈을 의존성 주입으로 사용할 수 없어서 생성자에 넣어서 사용하도록 한다
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
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

        /*List<String> roles = user.getUser().getAuthList()
                .stream()
                .map((auth) -> auth.getAuth())
                .collect(Collectors.toList());*/
        UserAuth role = user.getUser().getAuth();
        // JWT 토큰 생성 요청
        String jwt = jwtTokenProvider.createToken(userNo, userId, role);

        // { Authorization : Bearer + {jwt} }
        response.addHeader(JwtConstants.TOKEN_HEADER, JwtConstants.TOKEN_PREFIX + jwt);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
