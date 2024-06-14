package site.joshua.am.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HomeAttendanceCountDto {

    private LocalDateTime attendanceDate;
    private Long totalMember;

    public HomeAttendanceCountDto(LocalDateTime attendanceDate, Long totalMember) {
        this.attendanceDate = attendanceDate;
        this.totalMember = totalMember;
    }
}
