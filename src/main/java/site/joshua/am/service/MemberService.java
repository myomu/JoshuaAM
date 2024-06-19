package site.joshua.am.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.joshua.am.domain.Group;
import site.joshua.am.domain.Member;
import site.joshua.am.domain.MemberStatus;
import site.joshua.am.form.EditMemberForm;
import site.joshua.am.repository.GroupRepository;
import site.joshua.am.repository.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    /**
     * 회원 추가
     */
    @Transactional
    public Long join(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * Member 추가
     */
    @Transactional
    public Long addMember(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    // Member 를 수정할 때 Group 이 있을 경우와 null 일 경우를 분리해서 처리해준다.
    /**
     * Member 수정
     */
    @Transactional
    public void editMember(EditMemberForm form, Long memberId) {
        log.info("Test EditMemberForm : {}", form.toString());
        Member findMember = memberRepository.findOne(memberId);
        if (form.getGroup() != null && form.getGroup() != -1) {
            Group findGroup = groupRepository.findOne(form.getGroup());
            findMember.editMember(form.getName(), form.getBirthdate(), form.getGender(), findGroup);
        } else {
            findMember.nullifyGroupId(form.getName(), form.getBirthdate(), form.getGender());
        }
    }

    /**
     * Member 삭제 요청시 Member 의 Status 를 NON_MEMBER 로 변환시킨다.
     */
    @Transactional
    public void changeNonMember(List<Long> memberIds) {
        for (Long memberId : memberIds) {
            Member findMember = memberRepository.findOne(memberId);
            findMember.changeMemberStatus(MemberStatus.NON_MEMBER); // 비회원으로 변경
        }
    }

    /**
     * DB 에서 Member 완전 삭제
     */
    @Transactional
    public void deleteMember(List<Long> memberIds) {
        for (Long memberId : memberIds) {
            Member findMember = memberRepository.findOne(memberId);
            memberRepository.delete(findMember);
        }
    }

}
