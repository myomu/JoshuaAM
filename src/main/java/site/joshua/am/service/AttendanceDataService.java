package site.joshua.am.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.joshua.am.domain.AttendanceData;
import site.joshua.am.dto.AttendanceDataDto;
import site.joshua.am.repository.AttendanceDataRepository;

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
    public void addAttendanceData(AttendanceData attendanceData) {
        attendanceDataRepository.save(attendanceData);
    }

    /**
     * 해당되는 attendanceId 값을 가지는 AttendanceData 들을 반환
     */
    public List<AttendanceDataDto> findAttendanceDataList(Long attendanceId) {
        return attendanceDataRepository.findAttendanceDataById(attendanceId);
    }

}
