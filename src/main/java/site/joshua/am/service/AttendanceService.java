package site.joshua.am.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.joshua.am.domain.Attendance;
import site.joshua.am.domain.AttendanceData;
import site.joshua.am.domain.AttendanceStatus;
import site.joshua.am.dto.AttendanceDataDto;
import site.joshua.am.dto.AttendanceDto;
import site.joshua.am.form.EditAttendanceCheckForm;
import site.joshua.am.repository.AttendanceDataRepository;
import site.joshua.am.repository.AttendanceRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceDataRepository attendanceDataRepository;

    /**
     * 출석 추가
     */
    @Transactional
    public void addAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
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

        //받아온 form 의 날짜와 차이가 있으면 수정
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

    /**
     * 출석 삭제
     */
    @Transactional
    public void deleteAttendances(List<Long> attendanceIds) {
        for (Long attendanceId : attendanceIds) {
            Attendance findAttendance = attendanceRepository.findOne(attendanceId);
            attendanceRepository.delete(findAttendance);
        }
    }

    /**
     * /attendances 요청에 따른 출석 List 를 반환한다.
     * 먼저 모든 Attendance 를 찾고 각 Attendance 의 id 에 해당하는 AttendanceData 를 찾는다.
     * 그리고 AttendancesDto 에 넣고 이것을 다시 AttendancesDtoList 에 넣어서 반환한다.
     */
    public List<AttendanceDto> getAttendances() {
        List<Attendance> attendances = findAttendances();
        List<AttendanceDto> attendanceDtoList = new ArrayList<>();

        List<AttendanceDataDto> attendanceDataV2 = attendanceDataRepository.findAttendanceDataV2();
        for (Attendance attendance : attendances) {
            List<AttendanceDataDto> selectedAttendanceData = new ArrayList<>();

            for (AttendanceDataDto attendanceDataDto : attendanceDataV2) {
                if (attendance.getId().equals(attendanceDataDto.getAttendanceId())) {
                    selectedAttendanceData.add(attendanceDataDto);
                }
            }
            AttendanceDto attendanceDto = new AttendanceDto();
            attendanceDto.createAttendancesDto(attendance.getId(), attendance.getAttendanceDate(), selectedAttendanceData, selectedAttendanceData.size());
            attendanceDtoList.add(attendanceDto);
        }

        return attendanceDtoList;
    }

}
