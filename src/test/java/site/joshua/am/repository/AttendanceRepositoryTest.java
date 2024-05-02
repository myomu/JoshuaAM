package site.joshua.am.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AttendanceRepositoryTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Test
    void countAttendanceByAttendanceDate() {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 5, 1, 0, 0);
        Long longs = attendanceRepository.countAttendanceByAttendanceDate(startDate, endDate);
        System.out.println(longs.toString());
    }
}