package site.joshua.am.apicontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.joshua.am.domain.*;
import site.joshua.am.dto.*;
import site.joshua.am.form.AttendanceForm;
import site.joshua.am.form.CreateAttendanceCheckForm;
import site.joshua.am.form.DeleteAttendanceForm;
import site.joshua.am.form.EditAttendanceCheckForm;
import site.joshua.am.repository.AttendanceDataRepository;
import site.joshua.am.repository.AttendanceRepository;
import site.joshua.am.repository.GroupRepository;
import site.joshua.am.repository.MemberRepository;
import site.joshua.am.service.AttendanceDataService;
import site.joshua.am.service.AttendanceService;
import site.joshua.am.service.GroupService;
import site.joshua.am.service.MemberService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    /**
     * 출석 체크 화면 GET 요청
     */
    @GetMapping("/attendances/check")
    public List<ListOfMembersDto> listOfMembers() {
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

        //Member를 모두 불러와서 Form에서 Check 된 Member만 AttendanceData의 Status를 ATTENDANCE 로 저장.
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            AttendanceData attendanceData = new AttendanceData();
            if (form.getMemberIds().contains(member.getId())) {
                attendanceData.createAttendanceData(attendance, member, AttendanceStatus.ATTENDANCE);
            } else {
                attendanceData.createAttendanceData(attendance, member, AttendanceStatus.ABSENCE);
            }
            log.info("attendanceData={}", attendanceData);
            log.info("findAttendance={}", attendance);
            attendanceDataService.addAttendanceData(attendanceData);
        }
    }

    /**
     * 출석 목록 화면 GET 요청
     */
    @GetMapping("/attendances")
    public List<AttendancesDto> attendanceList() {
        List<Attendance> attendances = attendanceService.findAttendances();

        List<AttendancesDto> attendancesDtoList = new ArrayList<>();
        for (Attendance attendance : attendances) {
            List<AttendanceDataDto> attendanceDataList = attendanceDataService.findAttendanceDataList(attendance.getId());
            if (attendanceDataList.isEmpty()) {
                continue;
            }
            log.info("attendanceDataList={}", Arrays.deepToString(attendanceDataList.toArray()));
            AttendancesDto attendancesDto = new AttendancesDto();
            attendancesDto.createAttendancesDto(attendance.getId(), attendance.getAttendanceDate(), attendanceDataList, attendanceDataList.size());
            attendancesDtoList.add(attendancesDto);
        }

        return attendancesDtoList;
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
    @PostMapping("/attendances")
    public void deleteAttendance(@RequestBody @Valid DeleteAttendanceForm form) {
        log.info("form={}", form);
        attendanceService.deleteAttendances(form.getAttendanceIds());
    }



    //이전 코드
    /**
     * 출석 체크 수정 화면 GET 요청
     */
    @GetMapping("/attendances/{dateTime}/edit")
    public String editAttendances(Model model, @PathVariable("dateTime") LocalDateTime dateTime) {

        // 출석 체크 체크박스 테이블을 만들기 위한 데이터를 가져온다.
        List<Member> members = memberService.findMembers();
        List<Group> groups = groupService.findGroups();

        // 받아온 날짜를 기준으로 Attendance 를 불러옴. 이후 memberId를 List 에 저장해서 AttendanceForm 형태로 저장한다.
        List<Attendance> attendancesByDateTime = attendanceService.findAttendancesByDateTime(dateTime);
        List<Long> memberIds = new ArrayList<>();
        AttendanceForm form = new AttendanceForm();

        for (Attendance attendance : attendancesByDateTime) {
//            memberIds.add(attendance.getMember().getId());
        }

        form.setMemberIds(memberIds);

        model.addAttribute("members", members);
        model.addAttribute("groups", groups);
        model.addAttribute("attendanceForm", form);

        return "attendances/editAttendanceForm";
    }

    /**
     * 출석 체크 수정
     */
    @PostMapping("/attendances/{dateTime}/edit")
    public String editMember(@PathVariable("dateTime") LocalDateTime dateTime,
                             @RequestParam(name = "memberIds", required = false) List<Long> memberIds,
                             @RequestParam(name = "year", required = false) String year,
                             @RequestParam(name = "month", required = false) String month,
                             @RequestParam(name = "day", required = false) String day) {

        // 수정 시 아무것도 체크를 하지 않으면 수정 화면으로 다시 redirect.
        if (memberIds == null) {
            return "redirect:/attendances/{dateTime}/edit";
        }

        // 기존 LocalDateTime 을 저장. 만약 시간을 다시 설정하면 dateTime 이 변경됨으로 아래에서 기존의 출석들을 삭제할 때 사용.
        LocalDateTime preDateTime = dateTime;

        if (year != null && month != null && day != null) {
            String date = year+"-"+month+"-"+day+" 00:00:00";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            dateTime = LocalDateTime.parse(date, formatter);
        }

        // 기존의 dateTime 인 Attendance 들을 삭제 (먼저 데이터를 삭제하고 추가해야 한다. 추가하고 삭제하면 체크 된 값이 두번 나타나게 되는 문제가 발생한다.)
        List<Attendance> attendancesByDateTime = attendanceService.findAttendancesByDateTime(preDateTime);

        for (Attendance attendance : attendancesByDateTime) {
            Long id = attendance.getId();
            attendanceService.deleteAttendance(id);
        }

        // 새로 체크한 것을 기준으로 Attendance 들을 생성
        for (Long memberId : memberIds) {
            Attendance attendance = new Attendance();
            Member member = new Member();
            member.setMemberId(memberId);

//            attendance.createAttendance(member, AttendanceStatus.ATTENDANCE, dateTime);
            attendanceService.addAttendance(attendance);
        }

        return "redirect:/attendances/status";
    }

    /**
     * 출석 체크 삭제
     */
    @PostMapping("/attendances/{dateTime}/delete")
    public String deleteMember(@PathVariable("dateTime") LocalDateTime dateTime) {
        List<Attendance> attendances = attendanceService.findAttendancesByDateTime(dateTime);
        for (Attendance attendance : attendances) {
            attendanceService.deleteAttendance(attendance.getId());
        }

        return "redirect:/attendances/status";
    }

//    @Getter
//    static class AttendanceDto {
//        List<Member> members;
//        List<Group> groups;
//
//        public AttendanceDto(List<Member> members, List<Group> groups) {
//            this.members = members;
//            this.groups = groups;
//        }
//    }

}
