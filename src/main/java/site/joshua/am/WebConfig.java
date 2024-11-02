package site.joshua.am;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.joshua.am.prop.CorsProp;

import java.util.Arrays;

/**
 * Spring Interceptor 설정
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CorsProp corsProp;
    private final RateLimitConfig.RateLimitInterceptor rateLimitInterceptor;

    /**
     * Rate Limiting Interceptor 추가
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/**"); // 모든 경로에 인터셉터 추가
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(corsProp.getCorsAllow1());
        config.addAllowedOrigin(corsProp.getCorsAllow2());
        config.addAllowedOrigin(corsProp.getCorsAllow3());
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addExposedHeader("Authorization");
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
