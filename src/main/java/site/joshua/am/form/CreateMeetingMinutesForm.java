package site.joshua.am.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter
public class CreateMeetingMinutesForm {

    @NotEmpty(message = "제목은 필수 입니다")
    private String title;

    private String content;
    private Long userId;
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "CreateMinutesForm{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                '}';
    }
}
