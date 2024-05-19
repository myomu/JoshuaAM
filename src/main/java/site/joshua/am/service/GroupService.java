package site.joshua.am.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.joshua.am.domain.Group;
import site.joshua.am.domain.Member;
import site.joshua.am.repository.GroupRepository;
import site.joshua.am.repository.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    /**
     * 조 추가
     */
    @Transactional
    public Long addGroup(Group group) {
        groupRepository.save(group);
        return group.getId();
    }

    /**
     * 모든 조를 찾는다.
     */
    public List<Group> findGroups() {
        return groupRepository.findAll();
    }

    /**
     * groupId 에 해당하는 회원을 찾는다.
     */
    public Group findOne(Long groupId) {
        return groupRepository.findOne(groupId);
    }

    /**
     * 조 수정
     */
    @Transactional
    public void editGroup(Long groupId, String name) {
        Group group = groupRepository.findOne(groupId);
        group.editGroup(name);
    }

    /**
     * 조 삭제
     */
    @Transactional
    public void deleteGroups(List<Long> groupIds) {
        for (Long groupId : groupIds) {
            Group findGroup = groupRepository.findOne(groupId);
            groupRepository.delete(findGroup);
        }
    }


    // 이전 코드

    /**
     * 조 삭제
     */
    @Transactional
    public Long deleteGroup(Long groupId) {

        // 삭제할 group 의 id 키를 외래키로 가지는 Member 를 다 찾아서 외래키 부분을 null 로 만들어 준다.
        List<Member> findMembersByGroupId = memberRepository.findAllByGroupId(groupId);
        for (Member member : findMembersByGroupId) {
            member.setNullGroupFK();
        }


        // 이후 해당 group 을 삭제한다.
        Group group = groupRepository.findOne(groupId);
        groupRepository.delete(group);
        return groupId;
    }



}
