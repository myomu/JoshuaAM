package site.joshua.am.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.joshua.am.dto.HomeAttendanceCountDto;
import site.joshua.am.dto.LongTermAbsentee;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HomeRepositoryTest {

    @Autowired
    private HomeRepository homeRepository;

    @Test
    void findAttendanceCount() {
        List<HomeAttendanceCountDto> attendanceCount = homeRepository.findAttendanceCount();
        attendanceCount.sort(Comparator.comparing(HomeAttendanceCountDto::getAttendanceDate)); //페이징 할 때 desc 로 진행하므로 다시 이를 asc 로 정령해주기 위해 sort 를 사용
        System.out.println(attendanceCount);
    }

    @Test
    void findLongTermAbsentee() {
        List<LongTermAbsentee> longTermAbsentee = homeRepository.findLongTermAbsentee();
        System.out.println(longTermAbsentee);
    }
}