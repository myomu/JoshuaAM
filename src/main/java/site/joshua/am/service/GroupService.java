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
     * 그룹 추가
     */
    @Transactional
    public Long addGroup(Group group) {
        groupRepository.save(group);
        return group.getId();
    }

    /**
     * 모든 그룹을 찾는다.
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
     * 그룹 수정
     */
    @Transactional
    public void editGroup(Long groupId, String name) {
        Group group = groupRepository.findOne(groupId);
        group.editGroup(name);
    }

    /**
     * 그룹 삭제
     */
    @Transactional
    public void deleteGroups(List<Long> groupIds) {
        for (Long groupId : groupIds) {
            Group findGroup = groupRepository.findOne(groupId);
            List<Member> findMembers = memberRepository.findAllByGroupId(groupId);

            // 그룹을 삭제하기 전에 그룹에 속해있는 멤버들의 groupId 값을 null 로 바꿔준다. 이로써 외래키가 있음으로 삭제가 불가능한 경우를 피할 수 있다.
            for (Member member : findMembers) {
                member.nullifyGroupId(member.getName(), member.getBirthdate(), member.getGender());
            }
            groupRepository.delete(findGroup);
        }
    }

}
