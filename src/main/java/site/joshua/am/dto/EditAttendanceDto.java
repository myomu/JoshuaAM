package site.joshua.am.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EditAttendanceDto {
    private List<CheckedMemberIdsDto> checkedMemberIds;
    private LocalDateTime attendanceDate;

    public void createEditAttendanceDto(List<CheckedMemberIdsDto> checkedMemberIds, LocalDateTime attendanceDate) {
        this.checkedMemberIds = checkedMemberIds;
        this.attendanceDate = attendanceDate;
    }
}
