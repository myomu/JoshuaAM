package site.joshua.am.apicontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.joshua.am.dto.HomeAttendanceCountDto;
import site.joshua.am.dto.LongTermAbsentee;
import site.joshua.am.repository.HomeRepository;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
@Slf4j
public class HomeApiController {

    private final HomeRepository homeRepository;

    @GetMapping("/attendance")
    public ResponseEntity<?> AttendanceCount() {
        List<HomeAttendanceCountDto> attendanceCount = homeRepository.findAttendanceCount();

        if (attendanceCount != null) {
            attendanceCount.sort(Comparator.comparing(HomeAttendanceCountDto::getAttendanceDate)); //페이징 할 때 desc 로 진행하므로 다시 이를 asc 로 정령해주기 위해 sort 를 사용
            return new ResponseEntity<>(attendanceCount, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("출석 수를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/absentee")
    public ResponseEntity<?> getLongTermAbsentee() {
        List<LongTermAbsentee> longTermAbsentee = homeRepository.findLongTermAbsentee();
        if (longTermAbsentee != null) {
            return new ResponseEntity<>(longTermAbsentee, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("결석 인원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }
}
