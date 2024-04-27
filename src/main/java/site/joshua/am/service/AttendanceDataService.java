package site.joshua.am.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.joshua.am.domain.Attendance;
import site.joshua.am.domain.AttendanceData;
import site.joshua.am.dto.AttendanceDataDto;
import site.joshua.am.dto.AttendancesDto;
import site.joshua.am.repository.AttendanceDataRepository;
import site.joshua.am.repository.AttendanceRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceDataService {

    private final AttendanceDataRepository attendanceDataRepository;

    /**
     * 출석 추가
     */
    @Transactional
    public Long addAttendanceData(AttendanceData attendanceData) {
        attendanceDataRepository.save(attendanceData);
        return attendanceData.getId();
    }

    public List<AttendanceDataDto> findAttendanceDataList(Long attendanceId) {
        return attendanceDataRepository.findAttendanceData(attendanceId);
    }
}
