package site.joshua.am.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AttendancesDto {

    private Long groupId;
    private List<AttendanceCheckDto> attendanceCheckDtoList = new ArrayList<>();

    public AttendancesDto(Long groupId, List<AttendanceCheckDto> attendanceCheckDtoList) {
        this.groupId = groupId;
        this.attendanceCheckDtoList = attendanceCheckDtoList;
    }

    public AttendancesDto() {
    }

    public void addList(AttendanceCheckDto attendanceCheckDto) {
        this.attendanceCheckDtoList.add(attendanceCheckDto);
    }
}
