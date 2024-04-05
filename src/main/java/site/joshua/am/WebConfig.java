package site.joshua.am;

import org.springframework.context.annotation.Configuration;
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
@Configuration
public class WebConfig implements WebMvcConfigurer {

    //CORS(Cross Origin Resources
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:3000");
    }

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
