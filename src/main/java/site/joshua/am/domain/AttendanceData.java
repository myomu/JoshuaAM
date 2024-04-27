package site.joshua.am.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class AttendanceData {

    @Id @GeneratedValue
    @Column(name = "attendance_data_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    public void createAttendanceData(Attendance attendance, Member member, AttendanceStatus attendanceStatus) {
        this.attendance = attendance;
        this.member = member;
        this.attendanceStatus = attendanceStatus;
    }

    public void editAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}
