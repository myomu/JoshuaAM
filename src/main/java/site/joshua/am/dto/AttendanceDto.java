package site.joshua.am.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import site.joshua.am.domain.AttendanceStatus;
import site.joshua.am.domain.Member;

import java.time.LocalDateTime;

@Data
public class AttendanceDto {

//    @JsonIgnore
    private Long id;
    private LocalDateTime attendanceDate;
    private AttendanceStatus attendanceStatus;
    private Long memberId;

    public AttendanceDto(Long id, LocalDateTime attendanceDate, AttendanceStatus attendanceStatus, Long memberId) {
        this.id = id;
        this.attendanceDate = attendanceDate;
        this.attendanceStatus = attendanceStatus;
        this.memberId = memberId;
    }
}
