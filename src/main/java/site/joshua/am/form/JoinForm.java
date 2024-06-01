package site.joshua.am.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class JoinForm {

    @NotEmpty
    private String userLoginId;

    @NotEmpty
    private String userPw;

    @NotEmpty
    private String userName;

    @NotEmpty
    private String email;

    @NotEmpty
    private String authKey;

}
