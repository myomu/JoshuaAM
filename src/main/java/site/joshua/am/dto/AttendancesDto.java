package site.joshua.am.dto;

import lombok.Data;
import site.joshua.am.domain.AttendanceData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AttendancesDto {
    private Long attendanceId;
    private LocalDateTime attendanceDate;
    private List<AttendanceDataDto> attendanceDataDtoList = new ArrayList<>();
    private int totalMember;

    public void createAttendancesDto(Long attendanceId, LocalDateTime attendanceDate, List<AttendanceDataDto> attendanceDataDtoList, int totalMember) {
        this.attendanceId = attendanceId;
        this.attendanceDate = attendanceDate;
        this.attendanceDataDtoList = attendanceDataDtoList;
        this.totalMember = totalMember;
    }
}
