package site.joshua.am.apicontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.joshua.am.domain.*;
import site.joshua.am.dto.MemberDto;
import site.joshua.am.dto.MemberListDto;
import site.joshua.am.form.CreateMemberForm;
import site.joshua.am.form.DeleteMemberForm;
import site.joshua.am.form.EditMemberForm;
import site.joshua.am.repository.*;
import site.joshua.am.service.MemberService;
import site.joshua.am.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class MemberApiController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final GroupRepository groupRepository;
    private final AttendanceDataRepository attendanceDataRepository;
    private final AttendanceRepository attendanceRepository;

    /**
     * 멤버 생성
     */
    @PostMapping("members/create")
    public void createMember(@RequestBody @Valid CreateMemberForm form) {
        log.info("form={}", form);

//        User user = new User();
//        String password = "joshua" + form.getName() + "**";
//        user.createUser(form.getName(), password, form.getName(), "", UserAuth.USER);
//        userService.addUser(user);

        Member member = new Member();
        Group findGroup = groupRepository.findOne(form.getGroup());
        member.createMember(form.getName(), form.getAge(), form.getGender(), findGroup, MemberStatus.MEMBER);
        memberService.addMember(member);
    }

    /**
     * 멤버 목록 화면 요청
     */
    @GetMapping("/members")
    public List<MemberListDto> attendanceCheck(
            @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
            @RequestParam(name = "endDate", required = false) LocalDateTime endDate
    ) {

        log.info("startDate={}, endDate={}", startDate, endDate);
        Long countAttendance = attendanceRepository.countAttendanceByAttendanceDate(startDate, endDate);

        List<MemberListDto> members = memberRepository.findMembers();
        List<CountMemberByAttendanceDate> countMemberByAttendanceDates = attendanceDataRepository.countMemberAttendanceByAttendanceDate(startDate, endDate);

        //출석률 계산하여 DTO 에 추가
        for (MemberListDto member : members) {
            for (CountMemberByAttendanceDate countMember : countMemberByAttendanceDates) {
                if (member.getMemberId().equals(countMember.getMemberId())) {
                    Double rate = Math.round((double) countMember.getMemberAttendanceCount() / countAttendance * 10000) / 100.0;
                    member.setAttendanceRate(rate);
                    break;
                }
            }
        }

        return members;
    }

    /**
     * 멤버 수정 화면 요청
     */
    @GetMapping("/members/edit/{memberId}")
    public MemberDto findMemberById(@PathVariable Long memberId) {
        return memberRepository.findMember(memberId);
    }

    /**
     * 멤버 수정
     */
    @PostMapping("/members/edit/{memberId}")
    public void editMember(@RequestBody @Valid EditMemberForm form, @PathVariable Long memberId) {
        memberService.editMember(form, memberId);
    }

    /**
     * 멤버 삭제
     */
    @PostMapping("/members/delete")
    public void deleteMember(@RequestBody @Valid DeleteMemberForm form) {
        List<Long> memberIds = form.getMemberIds();
        memberService.deleteMember(memberIds);
    }

}
