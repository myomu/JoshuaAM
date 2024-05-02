package site.joshua.am.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.joshua.am.domain.CountMemberByAttendanceDate;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class AttendanceDataRepositoryTest {

    @Autowired
    AttendanceDataRepository adr;

    @Test
    void countMemberAttendanceByAttendanceDate() {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 5, 1, 0, 0);
        List<CountMemberByAttendanceDate> CountMemberByAttendanceDates = adr.countMemberAttendanceByAttendanceDate(startDate, endDate);
        System.out.println(CountMemberByAttendanceDates.toString());
    }
}