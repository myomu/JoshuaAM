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

}
