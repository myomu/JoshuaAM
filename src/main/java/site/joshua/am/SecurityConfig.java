package site.joshua.am;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.joshua.am.security.custom.CustomUserDetailService;
import site.joshua.am.security.jwt.filter.JwtAuthenticationFilter;
import site.joshua.am.security.jwt.filter.JwtRequestFilter;
import site.joshua.am.security.jwt.provider.JwtTokenProvider;
import site.joshua.am.service.RedisRefreshTokenService;

@Slf4j
@Configuration
@EnableWebSecurity
// @preAuthorize, @postAuthorize, @Secured 활성화
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisRefreshTokenService redisRefreshTokenService;
    private final CorsConfig corsConfig;

    // SpringSecurity 5.5 이상
    // 시큐리티 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("시큐리티 설정...");

        // 폼 기반 로그인 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);

        // HTTP 기본 인증 비활성화
        http.httpBasic(AbstractHttpConfigurer::disable);

        // CSRF(Cross-Site Request Forgery) 공격 방어 기능 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // 필터 설정
        // corsFilter -> JwtRequestFilter -> JwtAuthenticationFilter 순서
        http.addFilterAt(new JwtAuthenticationFilter(authenticationManager, jwtTokenProvider, redisRefreshTokenService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(corsConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtRequestFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        // 인가 설정
        http.authorizeHttpRequests(
                authorizeRequests ->
                authorizeRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 서버측 정적 자원(static) 요청 허가
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/api/users/join").permitAll()
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/api/auth/refresh-token").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
        );

        // 인증 방식 설정
        http.userDetailsService(customUserDetailService);

        return http.build();
    }

    // PasswordEncoder 빈 등록
    // 암호화 알고리즘 방식 : Bcrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AuthenticationManager authenticationManager;

    // AuthenticationManager 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        return this.authenticationManager;
    }

}
