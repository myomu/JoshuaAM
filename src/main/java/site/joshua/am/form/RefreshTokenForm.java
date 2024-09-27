package site.joshua.am.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RefreshTokenForm {

    private Long userId;

    public RefreshTokenForm(Long userId) {
        this.userId = userId;
    }
}
