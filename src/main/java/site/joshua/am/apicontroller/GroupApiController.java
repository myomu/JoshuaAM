package site.joshua.am.apicontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.joshua.am.domain.Group;
import site.joshua.am.dto.GroupDto;
import site.joshua.am.form.CreateGroupForm;
import site.joshua.am.form.DeleteGroupForm;
import site.joshua.am.form.EditGroupForm;
import site.joshua.am.repository.GroupRepository;
import site.joshua.am.service.GroupService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class GroupApiController {

    private final GroupRepository groupRepository;
    private final GroupService groupService;

    /**
     * 그룹 추가
     */
    @PostMapping("/groups/create")
    public void createGroup(@RequestBody @Valid CreateGroupForm form, BindingResult result) {

        log.info("form={}", form);

        // API 예외 처리로 교체할 예정.
//        if (result.hasErrors()) {
//            return "groups/createGroupForm";
//        }

        Group group = new Group();
        group.createGroup(form.getGroupName());
        groupService.addGroup(group);

    }

    /**
     * 그룹 목록 화면 요청
     */
    @GetMapping("/groups")
    public List<GroupDto> findGroups() {
        return groupRepository.findGroups();
    }

    /**
     * 그룹 수정 화면 요청
     */
    @GetMapping("/groups/{groupId}")
    public GroupDto findGroupById(@PathVariable Long groupId) {
        return groupRepository.findGroup(groupId);
    }

    /**
     * 그룹 수정
     */
    @PostMapping("/groups/edit/{groupId}")
    public void editGroup(@RequestBody @Valid EditGroupForm form, @PathVariable Long groupId) {
        log.info("groupId={}", groupId);
        groupService.editGroup(groupId, form.getGroupName());
    }

    /**
     * 그룹 삭제
     */
    @PostMapping("/groups/delete")
    public void deleteGroup(@RequestBody @Valid DeleteGroupForm form) {
        log.info("form={}", form);
        List<Long> groupIds = form.getGroupIds();
        groupService.deleteGroups(groupIds);
    }

}
