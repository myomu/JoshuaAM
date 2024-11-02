package site.joshua.am.security.jwt.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import site.joshua.am.domain.CustomUser;
import site.joshua.am.domain.User;
import site.joshua.am.domain.UserAuth;
import site.joshua.am.prop.JwtProp;
import site.joshua.am.repository.UserRepository;
import site.joshua.am.security.RefreshToken;
import site.joshua.am.security.jwt.constants.JwtConstants;
import site.joshua.am.service.RedisRefreshTokenService;


import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * JWT 토큰 관련 기능을 제공해주는 클래스
 * ✅ 토큰 생성
 * ✅ 토큰 해석
 * ✅ 토큰 유효성 검사
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProp jwtProp;
    private final UserRepository userRepository;
    private final RedisRefreshTokenService redisRefreshTokenService;
//    private final UserMapper userMapper;

    /**
     * JWT 토큰 생성 메서드
     * @param username 사용자 아이디
     * @param role 사용자 권한
     * @param expirationTime 만료 시간 (밀리초)
     * @return 생성된 JWT 토큰
     */
    // 액세스 토큰 생성(Create Access Token)
    public String createToken(String deviceId, String username, UserAuth role, long expirationTime) {

        // JWT 토큰 생성
        return Jwts.builder()
                // .signWith( 시크릿키, 알고리즘 )
                .signWith(getShaKey(), Jwts.SIG.HS512) // 시그니처에서 사용할 시크릿키, 알고리즘 설정
                .header()                                                 // 헤더 설정
                .add("typ", JwtConstants.TOKEN_TYPE)              // typ : JWT
                .and()
                .expiration(new Date(System.currentTimeMillis() + expirationTime)) //토큰 만료 시간 설정 (30분 - 1000 * 60 * 30 => 1800.000s)
                // .claim("uno", "" + userNo) // 클레임 설정 : 사용자 번호
                .claim("deviceId", deviceId) // deviceId : 로그인 한 기기의 고유 번호
                .claim("username", username) // PAYLOAD - username : 사용자 아이디
                .claim("rol", role) // PAYLOAD - rol : [ROLE_USER, ROLE,ADMIN] (권한 정보)
                .compact(); // 최종적으로 토큰 생성
    }

    /**
     * JWT 토큰 해석 메서드
     * @param authHeader Authorization 헤더 (Bearer {jwt})
     * @return 인증된 사용자 정보
     */
    public UsernamePasswordAuthenticationToken getAuthentication(String authHeader, String refreshToken) {
        if(authHeader == null || authHeader.isEmpty()) return null;

        try {
            // accessToken(jwt) 추출
            String accessToken = authHeader.replace(JwtConstants.TOKEN_PREFIX, "");

            // JWT 파싱
            Jws<Claims> parsedToken = parseToken(accessToken);

            // 인증된 사용자 번호
            //String userNo = parsedToken.getPayload().get("uno").toString();
            //Long no = ( userNo == null ? 0 : Long.parseLong(userNo) );

            RefreshToken findRefreshToken = redisRefreshTokenService.getRefreshToken("refreshToken:" + refreshToken);

            if (findRefreshToken == null) {
                log.error("Refresh token not found");
                return null;
            }

            Long userId = findRefreshToken.getUserId();

            // 인증된 사용자 아이디
            String username = parsedToken.getPayload().get("username").toString();

            // 인증된 사용자 권한
            String role = parsedToken.getPayload().get("rol").toString();

            // 토큰에 id, userId 있는지 확인
            if( username == null || username.isEmpty()) return null;

            User user = userRepository.findOne(userId);
            if (user == null) return null;

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));

            UserDetails userDetails = new CustomUser(user);

            // OK
            // new UsernamePasswordAuthenticationToken( 사용자정보객체, 비밀번호, 사용자의 권한(목록)  );
            return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

            // JwtException 은 UnsupportedJwtException, MalformedJwtException, ExpiredJwtException 등을 포함하는 예외처리이다.
        } catch (JwtException | IllegalArgumentException exception) {
            log.warn("Request to parse JWT : {} failed : {}", authHeader, exception.getMessage());
            return null;
        }

    }

    /**
     * 토큰 유효성 검사
     * - 만료기간이 넘었는지 판단
     * @param accessToken (jwt)
     * @return
     *  ⭕ true : 유효
     *  ❌ false : 만료
     */
    public boolean validateToken(String accessToken, String requestDeviceId) {

        try {
            // 🔐➡👩‍💼 JWT 파싱
            Jws<Claims> parsedToken = parseToken(accessToken);
            log.info("##### 토큰 만료 기간 ##### -> {}", parsedToken.getPayload().getExpiration());

            String parsedDeviceId = parsedToken.getPayload().get("deviceId").toString();

            // 요청 deviceId 와 accessToken 의 deviceId 값이 다르면 validation fail.
            if (!requestDeviceId.equals(parsedDeviceId)) {
                log.error("request deviceId is different from parsedDeviceId(AccessToken) : {}, {}", requestDeviceId, parsedDeviceId);
                return false;
            }

            Date exp = parsedToken.getPayload().getExpiration();

            // 만료 시간과 현재 시간 비교
            // 2024.05.01 vs 2024.05.11 --> 만료 : true --> false
            // 2024.05.30 vs 2024.05.11 --> 유효 : false --> true
            return !exp.before(new Date());

        } catch (ExpiredJwtException exception) {
            log.error("Token Expired");                 // 토큰 만료
            return false;
        } catch (JwtException exception) {
            log.error("Token Tampered");                // 토큰 손상 (서버측 시크릿 키와 다르다거나 토큰이 위변조 되었을 때 등)
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is null");                 // 토큰 없음
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * JWT 토큰 파싱 메서드
     * @param token JWT 토큰
     * @return 파싱된 클레임
     */
    public Jws<Claims> parseToken(String token) {
        try {
            // 이 부분을 아래와 같이 만듦.
            /*Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);*/
            return Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token");
            throw e;
        } catch (JwtException e) {
            log.warn("Invalid JWT token");
            throw e;
        }
    }

    // secretKey -> signingKey
    private byte[] getSigningKey() {
        // log.info("getSigningKey : {} - key.length() : {}", jwtProp.getSecretKey(), jwtProp.getSecretKey().getBytes().length);
        return jwtProp.getSecretKey().getBytes();
    }

    // secretKey -> (HMAC-SHA algorithms) -> signingKey
    private SecretKey getShaKey() {
        return Keys.hmacShaKeyFor(getSigningKey());
    }


}
