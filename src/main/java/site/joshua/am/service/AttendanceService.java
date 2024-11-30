package site.joshua.am.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.joshua.am.domain.Attendance;
import site.joshua.am.domain.AttendanceData;
import site.joshua.am.domain.AttendanceStatus;
import site.joshua.am.domain.Member;
import site.joshua.am.dto.AttendanceDataDto;
import site.joshua.am.dto.AttendanceDto;
import site.joshua.am.form.EditAttendanceCheckForm;
import site.joshua.am.repository.AttendanceDataRepository;
import site.joshua.am.repository.AttendanceRepository;
import site.joshua.am.repository.MemberRepository;

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
    private final MemberRepository memberRepository;
    private final AttendanceDataService attendanceDataService;

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
    public List<Attendance> findAttendances(Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }

    /**
     * 출석들을 찾아 List 로 반환 할 때, AttendanceDate 또는 totalMember 에 따라 정렬해서 반환
     */
    public List<Attendance> findAttendancesBySort(Pageable pageable) {
        return attendanceRepository.findAttendanceBySort(pageable);
    }



    /**
     * attendanceId 에 해당하는 출석을 찾는다.
     */
    public Attendance findOne(Long attendanceId) {
        return attendanceRepository.findOne(attendanceId);
    }

    /**
     * 출석 수정
     */
    @Transactional
    public void editAttendance(Long attendanceId, EditAttendanceCheckForm form) {

        LocalDateTime formAttendanceDate = form.getAttendanceDate();
        Attendance findAttendance = attendanceRepository.findOne(attendanceId);

        /*//받아온 form 의 날짜와 차이가 있으면 수정
        if (!formAttendanceDate.isEqual(findAttendance.getAttendanceDate())) {
            findAttendance.editDateTime(formAttendanceDate);
        }

        //attendanceId 로 Attendance 엔터티를 찾아서 해당 엔터티의 Id 값을 가지는 AttendanceData를 가져오고
        //가져온 데이터의 member Id 값이 전달받은 MemberIds 배열에 있으면 ATTENDANCE, 없으면 ABSENCE 로 변경한다.
        // 가져온 attendanceData 는 처음 생성 되었을 때를 기준으로 회원 값이 저장되어 있으므로 이후에 생성된 회원의 경우 인식하지 못하는 문제가 발생한다.
        // 이를 해결하기 위해 .. 기존의 attendanceData 를 전부 삭제하고 다시 추가하는 방법으로 진행해야 할 것 같다.
        List<AttendanceData> attendanceDataList = findAttendance.getAttendanceDataList();
        for (AttendanceData attendanceData : attendanceDataList) {
            //System.out.println(attendanceData.toString());
            if (form.getMemberIds().contains(attendanceData.getMember().getId())) {
                attendanceData.editAttendanceStatus(AttendanceStatus.ATTENDANCE);
            } else {
                attendanceData.editAttendanceStatus(AttendanceStatus.ABSENCE);
            }
        }*/

        // 기존의 Attendance 삭제 -> cascade 설정으로 자동으로 AttendanceId 값을 외래키로 가지고 있는 AttendanceData 삭제
        attendanceRepository.delete(findAttendance);

        //Attendance 를 먼저 생성하고 DB에 저장
        Attendance attendance = new Attendance();
        attendance.createAttendance(form.getAttendanceDate());
        addAttendance(attendance);

        //Member를 모두 불러와서 Form에서 Check 된 Member만 AttendanceData의 Status를 ATTENDANCE 로 저장.
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            AttendanceData attendanceData = new AttendanceData();
            if (form.getMemberIds().contains(member.getId())) {
                attendanceData.createAttendanceData(attendance, member, AttendanceStatus.ATTENDANCE);
            } else {
                attendanceData.createAttendanceData(attendance, member, AttendanceStatus.ABSENCE);
            }
            attendanceDataService.addAttendanceData(attendanceData);
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
    public List<AttendanceDto> getAttendances(Pageable pageable) {
        //List<Attendance> attendances = findAttendances(pageable);
        List<Attendance> attendances = findAttendancesBySort(pageable);
        List<AttendanceDto> attendanceDtoList = new ArrayList<>();
        List<AttendanceDataDto> attendanceData = attendanceDataRepository.findAllAttendanceData();

        for (Attendance attendance : attendances) {
            List<AttendanceDataDto> selectedAttendanceData = new ArrayList<>();

            for (AttendanceDataDto attendanceDataDto : attendanceData) {
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
