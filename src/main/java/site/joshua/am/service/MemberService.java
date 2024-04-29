package site.joshua.am.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.joshua.am.domain.Gender;
import site.joshua.am.domain.Group;
import site.joshua.am.domain.Member;
import site.joshua.am.form.EditMemberForm;
import site.joshua.am.repository.GroupRepository;
import site.joshua.am.repository.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

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

    @Transactional
    public Long addMember(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public void editMember(EditMemberForm form, Long memberId) {
        Member findMember = memberRepository.findOne(memberId);
        Group findGroup = groupRepository.findOne(form.getGroup());
        findMember.editMember(form.getName(), form.getAge(), form.getGender(), findGroup);
    }

    @Transactional
    public void deleteMember(List<Long> memberIds) {
        for (Long memberId : memberIds) {
            Member findMember = memberRepository.findOne(memberId);
            memberRepository.delete(findMember);
        }
    }

    // 이전 코드

    /**
     * 모든 회원을 찾는다.
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * memberId 에 해당하는 회원을 찾는다.
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
     * groupId 에 해당하는 회원들을 찾아 List 로 반환한다.
     */
    public List<Member> findMembersByGroupId(Long groupId) {
        return memberRepository.findAllByGroupId(groupId);
    }

    /**
     * memberId 에 해당하는 회원의 정보를 수정한다.
     */
    @Transactional //변경 감지 방식
    public void editMember(Long memberId, String name, int age, Gender gender, Group group) {
        Member member = memberRepository.findOne(memberId);
        member.editMember(name, age, gender, group);
    }

    /**
     * memberId 에 해당하는 회원을 삭제한다.
     */
    @Transactional
    public Long deleteMember(Long memberId) {
        Member member = memberRepository.findOne(memberId);
        memberRepository.delete(member);
        return member.getId();
    }

}
