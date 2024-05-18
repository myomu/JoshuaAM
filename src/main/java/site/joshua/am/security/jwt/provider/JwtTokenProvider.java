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
import site.joshua.am.security.jwt.constants.JwtConstants;


import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
//    private final UserMapper userMapper;

    /**
     * 토큰 생성
     */
    public String createToken(Long userNo, String userId, UserAuth role) {

        // JWT 토큰 생성
        String jwt = Jwts.builder()
                // .signWith( 시크릿키, 알고리즘 )
                .signWith(getShaKey(), Jwts.SIG.HS512) // 시그니처에서 사용할 시크릿키, 알고리즘 설정
                .header()                                                 // 헤더 설정
                .add("typ", JwtConstants.TOkEN_TYPE)              // typ : JWT
                .and()
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) //토큰 만료 시간 설정 (30분)
                .claim("uno", "" + userNo) // 클레임 설정 : 사용자 번호
                .claim("uid", userId) // PAYLOAD - uid : user (사용자 아이디)
                .claim("rol", role) // PAYLOAD - rol : [ROLE_USER, ROLE,ADMIN] (권한 정보)
                .compact(); // 최종적으로 토큰 생성

        log.info("jwt : {}", jwt);

        return jwt;
    }

    /**
     * 🔐➡👩‍💼 토큰 해석
     *
     * Authorization : Bearer + {jwt}  (authHeader)
     * ➡ jwt 추출
     * ➡ UsernamePasswordAuthenticationToken
     * @param authHeader
     * @return
     * @throws Exception
     */
    public UsernamePasswordAuthenticationToken getAuthentication(String authHeader) {
        if(authHeader == null || authHeader.length() == 0 )
            return null;

        try {

            // jwt 추출
            String jwt = authHeader.replace(JwtConstants.TOKEN_PREFIX, "");

            // 🔐➡👩‍💼 JWT 파싱
            Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);

            log.info("parsedToken : {}", parsedToken);

            // 인증된 사용자 번호
            String userNo = parsedToken.getPayload().get("uno").toString();
            Long no = ( userNo == null ? 0 : Long.parseLong(userNo) );
            log.info("userNo : {}", userNo);

            // 인증된 사용자 아이디
            String userId = parsedToken.getPayload().get("uid").toString();
            log.info("userId : {}", userId);

            // 인증된 사용자 권한
            Claims claims = parsedToken.getPayload();
            Object roles = claims.get("rol");
            log.info("roles : {}", roles);


            // 토큰에 id, userId 있는지 확인
            if( userId == null || userId.isEmpty())
                return null;

            User user = new User();

            try {
                User userInfo = userRepository.findOne(no);
                if (userInfo != null) {
                    user.setAuthToUser(no, userId, userInfo.getUserName(), userInfo.getEmail(), UserAuth.valueOf(roles.toString()));
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("토큰 유효 -> DB 추가 정보 조회시 에러 발생...");
            }



            /*user.setNo(no);
            user.setUserId(userId);
            // OK: 권한도 바로 Users 객체에 담아보기
            List<UserAuth> authList = ((List<?>) roles )
                    .stream()
                    .map(auth -> new UserAuth(userId, auth.toString()) )
                    .collect( Collectors.toList() );
            user.setAuthList(authList);*/

            // OK
            // CustomUser 에 권한 담기
//            List<SimpleGrantedAuthority> authorities = ((List<?>) roles )
//                    .stream()
//                    .map(auth -> new SimpleGrantedAuthority( (String) auth ))
//                    .collect( Collectors.toList() );
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority((String) roles));

            // 토큰 유효하면
            // name, email 도 담아주기
            /*try {
                User userInfo = userMapper.select(no);
                if( userInfo != null ) {
                    user.setName(userInfo.getName());
                    user.setEmail(userInfo.getEmail());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("토큰 유효 -> DB 추가 정보 조회시 에러 발생...");
            }*/



            UserDetails userDetails = new CustomUser(user);

            // OK
            // new UsernamePasswordAuthenticationToken( 사용자정보객체, 비밀번호, 사용자의 권한(목록)  );
            return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        } catch (ExpiredJwtException exception) {
            log.warn("Request to parse expired JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.warn("Request to parse unsupported JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.warn("Request to parse invalid JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.warn("Request to parse empty or null JWT : {} failed : {}", authHeader, exception.getMessage());
        }

        return null;
    }

    /**
     * 토큰 유효성 검사
     * - 만료기간이 넘었는지 판단
     * @param jwt
     * @return
     *  ⭕ true : 유효
     *  ❌ false : 만료
     */
    public boolean validateToken(String jwt) {

        try {
            // 🔐➡👩‍💼 JWT 파싱
            Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);

            log.info("##### 토큰 만료 기간 #####");
            log.info("-> {}", parsedToken.getPayload().getExpiration());

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

    // secretKey -> signingKey
    private byte[] getSigningKey() {
        return jwtProp.getSecretKey().getBytes();
    }

    // secretKey -> (HMAC-SHA algorithms) -> signingKey
    private SecretKey getShaKey() {
        return Keys.hmacShaKeyFor(getSigningKey());
    }


}
