package site.joshua.am.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class AttendanceStatusDTO {

    private LocalDateTime datetime;
    private int count;

}
