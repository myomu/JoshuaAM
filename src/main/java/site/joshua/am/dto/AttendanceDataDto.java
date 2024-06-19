package site.joshua.am.dto;

import lombok.Data;
import site.joshua.am.domain.AttendanceStatus;

@Data
public class AttendanceDataDto {
    private Long attendanceDataId;
    private Long memberId;
    private String memberName;
    private String attendanceStatus;
    private Long attendanceId;

    public AttendanceDataDto(Long attendanceDataId, Long memberId, String memberName, AttendanceStatus attendanceStatus, Long attendanceId) {
        this.attendanceDataId = attendanceDataId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.attendanceStatus = attendanceStatus.getDescription();
        this.attendanceId = attendanceId;
    }
}
