package site.joshua.am.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
@Data
@Table(name = "Users") //User는 테이블 에약어로 되어있어 오류가 발생.
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NotEmpty
    private String userLoginId;

    @NotEmpty
    private String userPw;

    @NotEmpty
    private String userName;

    @NotEmpty
    private String email;

    private Date regDate;
    private Date updDate;
    private int enabled; // 활성화 여부. 1은 활성화, 0은 비활성화

    @Enumerated(EnumType.STRING)
    private UserAuth auth;

    public void createUser(String userLoginId, String userPw, String userName, String email, Date regDate, Date updDate, UserAuth auth, int enabled) {
        this.userLoginId = userLoginId;
        this.userPw = userPw;
        this.userName = userName;
        this.email = email;
        this.regDate = regDate;
        this.updDate = updDate;
        this.auth = auth;
        this.enabled = enabled;
    }

    public void setEncodedPW(String encodedPW) {
        this.userPw = encodedPW;
    }

    public void setAuthToUser(Long id, String loginId, String userName, String email, UserAuth auth) {
        this.id = id;
        this.userLoginId = loginId;
        this.userName = userName;
        this.email = email;
        this.auth = auth;
    }

    public void setDisabled(boolean disabled) {
        this.enabled = disabled ? 1 : 0;
    }

    public void editPasswordAndNameAndEmail(String password, String userName, String email) {
        this.userPw = password;
        this.userName = userName;
        this.email = email;
    }
}
