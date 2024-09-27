package site.joshua.am.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Attendance {

    @Id @GeneratedValue
    @Column(name = "attendance_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime attendanceDate;

    @OneToMany(mappedBy = "attendance", cascade = CascadeType.REMOVE)
    private List<AttendanceData> attendanceDataList = new ArrayList<>();

    public void createAttendance(LocalDateTime attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public void editDateTime(LocalDateTime newAttendanceDate) {
        this.attendanceDate = newAttendanceDate;
    }

}
