package site.joshua.am.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EditUserForm {

    private Long id;

    @NotEmpty
    private String userLoginId;

    @NotEmpty
    private String userPw;

    @NotEmpty
    private String userName;

    @NotEmpty
    private String email;

}
