package site.joshua.am.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import site.joshua.am.domain.Gender;

@Data
@Getter @Setter // Setter 필수!!
public class EditMemberForm {

    @NotEmpty(message = "이름은 필수 입니다")
    private String name;

    private int dateOfBirth;
    private Gender gender;
    private Long group;

}
