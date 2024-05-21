    package site.joshua.am.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@ConfigurationProperties(prefix = "site.joshua.am.cors") // site.joshuaam.jwt 경로 하위 속성들을 지정
public class CorsProp {

    // CORS 허용 주소
    private String corsAllow1;
    private String corsAllow2;
    private String corsAllow3;
}
