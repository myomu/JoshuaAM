package site.joshua.am;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Interceptor 설정
 */
@Slf4j
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${site.joshua.am.cors-allow}")
    private String corsAllow;

    // CORS(Cross Origin Resource Sharing) 문제 해결법
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(corsAllow)
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // 이것을 추가해줘야 CORS 오류가 안난다. 기본값으로 GET, HEAD, POST 추가는 해준다.
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
