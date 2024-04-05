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

    private LocalDateTime attendanceDate;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL)
    private List<AttendanceData> attendanceDataList = new ArrayList<>();

    public void createAttendance(Member member, AttendanceStatus attendanceStatus, LocalDateTime attendanceDate) {
        this.member = member;
        this.attendanceStatus= attendanceStatus;
        this.attendanceDate = attendanceDate;
    }
}
