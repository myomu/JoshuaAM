package site.joshua.am.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Entity
@Getter
@Table(name = "Users") //User는 테이블 에약어로 되어있어 오류가 발생.
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;

    @NotEmpty
    private String userName;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserAuth auth;

    public void createUser(String loginId, String password, String userName, String email, UserAuth auth) {
        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
        this.email = email;
        this.auth = auth;
    }
}
