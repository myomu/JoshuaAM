package site.joshua.am.apicontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.joshua.am.domain.*;
import site.joshua.am.dto.*;
import site.joshua.am.form.CreateAttendanceCheckForm;
import site.joshua.am.form.DeleteAttendanceForm;
import site.joshua.am.form.EditAttendanceCheckForm;
import site.joshua.am.prop.CorsProp;
import site.joshua.am.repository.AttendanceDataRepository;
import site.joshua.am.repository.AttendanceRepository;
import site.joshua.am.repository.GroupRepository;
import site.joshua.am.repository.MemberRepository;
import site.joshua.am.service.AttendanceDataService;
import site.joshua.am.service.AttendanceService;
import site.joshua.am.service.GroupService;
import site.joshua.am.service.MemberService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AttendanceApiController {

    private final MemberService memberService;
    private final AttendanceService attendanceService;
    private final GroupService groupService;

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceDataService attendanceDataService;
    private final AttendanceDataRepository attendanceDataRepository;

    private final CorsProp corsProp;
    /**
     * 출석 체크 화면 GET 요청
     */
    @GetMapping("/attendances/check")
    public List<ListOfMembersDto> listOfMembers() {

        log.info("CorsProp : {}", corsProp);

        List<Group> groups = groupRepository.findAll();
        List<AttendanceMembersDto> members = attendanceRepository.findListOfMembers();
        List<ListOfMembersDto> listOfMembersDtos = new ArrayList<>();

        for (Group group : groups) {
            ListOfMembersDto listOfMembersDto = new ListOfMembersDto();
            listOfMembersDto.setGroupId(group.getId());
            listOfMembersDto.setGroupName(group.getName());
            for (AttendanceMembersDto member : members) {
                if (group.getId().equals(member.getGroupId())) {
                    listOfMembersDto.addList(member);
                }
            }
            listOfMembersDtos.add(listOfMembersDto);
        }

        return listOfMembersDtos;
    }

    /**
    * 출석 체크 생성
    */
    @PostMapping("/attendances/create")
    public void createAttendance(@RequestBody @Valid CreateAttendanceCheckForm form) {
        log.info("form={}", form);
        if (form.getMemberIds().isEmpty()) {
            //react에 대충 400.. 에러를 내려줘야하거나 출석체크 화면으로 이동하게끔(가능?) 해야할 듯
        }

        LocalDateTime dateTime;
        if (form.getAttendanceDate() == null) {
            dateTime = LocalDateTime.now();
        } else {
            dateTime = form.getAttendanceDate();
        }

        //Attendance 를 먼저 생성하고 DB에 저장
        Attendance attendance = new Attendance();
        attendance.createAttendance(dateTime);
        attendanceService.addAttendance(attendance);

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
     * 출석 목록 화면 GET 요청
     */
    //@GetMapping("/attendances")
    public List<AttendanceDto> attendanceListV1() {
        List<Attendance> attendances = attendanceService.findAttendances();

        List<AttendanceDto> attendanceDtoList = new ArrayList<>();
        for (Attendance attendance : attendances) {
            List<AttendanceDataDto> attendanceDataList = attendanceDataService.findAttendanceDataList(attendance.getId());
            if (attendanceDataList.isEmpty()) {
                continue;
            }
            log.info("attendanceDataList={}", Arrays.deepToString(attendanceDataList.toArray()));
            AttendanceDto attendanceDto = new AttendanceDto();
            attendanceDto.createAttendancesDto(attendance.getId(), attendance.getAttendanceDate(), attendanceDataList, attendanceDataList.size());
            attendanceDtoList.add(attendanceDto);
        }

        return attendanceDtoList;
    }

    /**
     * 출석 목록 화면 GET 요청 개선 버전
     */
    @GetMapping("/attendances")
    public List<AttendanceDto> attendanceListV2() {
        return attendanceService.getAttendances();
    }



    /**
     * 출석 체크 수정 화면 요청
     */
    @GetMapping("/attendances/edit/{attendanceId}")
    public EditAttendanceDto checkedAttendanceMembers(@PathVariable("attendanceId") Long attendanceId) {
        List<CheckedMemberIdsDto> selectedMembers = attendanceDataRepository.findSelectedMembers(attendanceId);
        Attendance findAttendance = attendanceRepository.findOne(attendanceId);
        LocalDateTime attendanceDate = findAttendance.getAttendanceDate();
        EditAttendanceDto editAttendanceDto = new EditAttendanceDto();
        editAttendanceDto.createEditAttendanceDto(selectedMembers, attendanceDate);

        return editAttendanceDto;
    }

    /**
     * 출석 체크 수정
     */
    @PostMapping("/attendances/edit/{attendanceId}")
    public void editAttendance(@RequestBody @Valid EditAttendanceCheckForm form, @PathVariable("attendanceId") Long attendanceId) {
        log.info("form={}", form);
        if (form.getMemberIds().isEmpty()) {
            //react에 대충 400.. 에러를 내려줘야하거나 출석체크 화면으로 이동하게끔(가능?) 해야할 듯
        }
        attendanceService.editAttendance(attendanceId, form);
    }

    /**
     * 출석 체크 삭제
     */
    @PostMapping("/attendances/delete")
    public void deleteAttendance(@RequestBody @Valid DeleteAttendanceForm form) {
        log.info("form={}", form);
        attendanceService.deleteAttendances(form.getAttendanceIds());
    }

}
