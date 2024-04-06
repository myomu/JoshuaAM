package site.joshua.am.apicontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.joshua.am.domain.*;
import site.joshua.am.dto.AttendanceCheckDto;
import site.joshua.am.dto.MembersDto;
import site.joshua.am.form.CreateMemberForm;
import site.joshua.am.repository.GroupRepository;
import site.joshua.am.repository.MemberRepository;
import site.joshua.am.repository.UserRepository;
import site.joshua.am.service.MemberService;
import site.joshua.am.service.UserService;

import java.util.List;

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

    @PostMapping("members/create")
    public void createMember(@RequestBody @Valid CreateMemberForm form) {
        log.info("form={}", form);

        User user = new User();
        String password = "joshua" + form.getName() + "**";
        user.createUser(form.getName(), password, form.getName(), "", UserAuth.USER);
        userService.addUser(user);

        Member member = new Member();
        Group findGroup = groupRepository.findOne(form.getGroup());
        member.createMember(form.getName(), form.getAge(), form.getGender(), findGroup, user, MemberStatus.MEMBER);
        memberService.addMember(member);
    }

    @GetMapping("/members")
    public List<MembersDto> attendanceCheck() {
        return memberRepository.findMembers();
    }

}
