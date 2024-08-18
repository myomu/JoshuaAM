package site.joshua.am.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter
public class EditMeetingMinutesForm {

    @NotEmpty(message = "제목은 필수 입니다")
    private String title;

    private String content;
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "EditMeetingMinutesForm{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
