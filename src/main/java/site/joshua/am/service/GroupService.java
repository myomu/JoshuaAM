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

}
