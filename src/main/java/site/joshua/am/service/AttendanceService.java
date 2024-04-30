package site.joshua.am.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.joshua.am.domain.Attendance;
import site.joshua.am.domain.AttendanceData;
import site.joshua.am.domain.AttendanceStatus;
import site.joshua.am.form.EditAttendanceCheckForm;
import site.joshua.am.repository.AttendanceRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    /**
     * 출석 추가
     */
    @Transactional
    public Long addAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
        return attendance.getId();
    }

    /**
     * 출석들을 찾아 List 로 반환
     */
    public List<Attendance> findAttendances() {
        return attendanceRepository.findAll();
    }

    /**
     * attendanceId 에 해당하는 출석을 찾는다.
     */
    public Attendance findOne(Long attendanceId) {
        return attendanceRepository.findOne(attendanceId);
    }

    @Transactional
    public void editAttendance(Long attendanceId, EditAttendanceCheckForm form) {

        LocalDateTime formAttendanceDate = form.getAttendanceDate();
        Attendance findAttendance = attendanceRepository.findOne(attendanceId);

        //받아온 form의 날짜와 차이가 있으면 수정
        if (!formAttendanceDate.isEqual(findAttendance.getAttendanceDate())) {
            findAttendance.editDateTime(formAttendanceDate);
        }

        //attendanceId 로 Attendance 엔터티를 찾아서 해당 엔터티의 Id 값을 가지는 AttendanceData를 가져오고
        //가져온 데이터의 member Id 값이 전달받은 MemberIds 배열에 있으면 ATTENDANCE, 없으면 ABSENCE 로 변경한다.
        List<AttendanceData> attendanceDataList = findAttendance.getAttendanceDataList();
        for (AttendanceData attendanceData : attendanceDataList) {
            if (form.getMemberIds().contains(attendanceData.getMember().getId())) {
                attendanceData.editAttendanceStatus(AttendanceStatus.ATTENDANCE);
            } else {
                attendanceData.editAttendanceStatus(AttendanceStatus.ABSENCE);
            }
        }
    }

    @Transactional
    public void deleteAttendances(List<Long> attendanceIds) {
        for (Long attendanceId : attendanceIds) {
            Attendance findAttendance = attendanceRepository.findOne(attendanceId);
            attendanceRepository.delete(findAttendance);
        }
    }

    //이전 코드

    /**
     * 중복되지 않는 날짜를 List 로 반환. 내림차순 정렬로 보낸다.
     */
    public List<LocalDateTime> findNoDuplicateDate() {
        return attendanceRepository.findNoDuplicateDate();
    }

    /**
     * dateTime 에 해당하는 출석들을 찾아 List 로 반환
     */
    public List<Attendance> findAttendancesByDateTime(LocalDateTime dateTime) {
        return attendanceRepository.findAllByDateTime(dateTime);
    }

    /**
     * 출석 삭제
     */
    @Transactional
    public Long deleteAttendance(Long attendanceId) {
        Attendance attendance = attendanceRepository.findOne(attendanceId);
        attendanceRepository.delete(attendance);
        return attendance.getId();
    }
}
