    package site.joshua.am.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("site.joshua.am.jwt") // site.joshuaam.jwt 경로 하위 속성들을 지정
public class JwtProp {

    // 시크릿키 : JWT 시그니처 암호화를 위한 정보
    private String secretKey;
}
