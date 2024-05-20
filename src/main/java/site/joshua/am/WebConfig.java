package site.joshua.am;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.joshua.am.argumentresolver.LoginUserArgumentResolver;
import site.joshua.am.interceptor.LogInterceptor;
import site.joshua.am.interceptor.LoginCheckInterceptor;

import java.util.List;

/**
 * Spring Interceptor 설정
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${site.joshua.am.cors-allow}")
    private String corsAllow;


    //CORS(Cross Origin Resource Sharing) 문제 해결법
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("corsAllow: {}", corsAllow);

        registry.addMapping("/**")
                .allowedOrigins(corsAllow, "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 이것을 추가해줘야 CORS 오류가 안난다. 기본값으로 GET, HEAD, POST 추가는 해준다.
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    //404 Not Found 에러 문제 해결법
    //react 는 route 를 통해 이동을 하게 되고 url 이 변경되는데 이를 서버에 요청해도 반환되는 페이지가 없어서 발생하는 문제
//    @Bean
//    public ErrorPageRegistrar errorPageRegistrar() {
//        return new AddErrorPageRegistrar();
//    }
//
//    private static class AddErrorPageRegistrar implements ErrorPageRegistrar {
//        @Override
//        public void registerErrorPages(ErrorPageRegistry registry) {
//            ErrorPage errorPage = new ErrorPage(HttpStatus.NOT_FOUND, "/");
//            registry.addErrorPages(errorPage);
//        }
//    }

//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(new LoginUserArgumentResolver());
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LogInterceptor())
//                .order(1)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/css/**", "/js/**", "/*.ico", "/error");
//
//        registry.addInterceptor(new LoginCheckInterceptor())
//                .order(2)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/", "/login", "/logout", "/css/**", "/js/**", "/*.ico", "/error");
//    }

}
