package site.joshua.am.security;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class RefreshToken {

    private String id; // UUID 값, 고유 식별자 및 토큰 값
    private Long userId; // 사용자(User) DB ID
    private String username; // 사용자 계정 ID
    private String role; // 사용자 권한
    private String userAgent; // 브라우저나 기기의 User-Agent 정보
    private String deviceId; // 사용자 기기의 고유 식별 번호

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime expiration; // 만료 시간

    public RefreshToken() {
    }

    public void generateRefreshToken(Long userId, String username, String role, String userAgent, String deviceId, LocalDateTime expiration) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.userAgent = userAgent;
        this.deviceId = deviceId;
        this.expiration = expiration;
    }
}
