package site.joshua.am;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.joshua.am.prop.CorsProp;

/**
 * Spring Interceptor 설정
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {

//    @Value("${site.joshua.am.cors-allow}")
//    private String corsAllow;
    private final CorsProp corsProp;

    // CORS(Cross Origin Resource Sharing) 문제 해결법
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins(corsAllow, "http://localhost:3000")
//                        .allowedMethods("GET", "POST", "PUT", "DELETE") // 이것을 추가해줘야 CORS 오류가 안난다. 기본값으로 GET, HEAD, POST 추가는 해준다.
//                        .allowedHeaders("*")
//                        .allowCredentials(true);
//            }
//        };
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(String.valueOf(corsProp), "http://localhost:3000", "https://joshua2024.site", "https://www.joshua2024.site", "https://web-joshuaam-front-1ru12mlwbog2dc.sel5.cloudtype.app")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Content-Type")
                .allowCredentials(true);
    }
}
