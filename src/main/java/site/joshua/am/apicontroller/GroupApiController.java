package site.joshua.am.apicontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.joshua.am.domain.Group;
import site.joshua.am.dto.GroupsDto;
import site.joshua.am.form.CreateGroupForm;
import site.joshua.am.form.GroupForm;
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
     * 조 추가
     */
    @PostMapping("/groups/create")
    public void createGroup(@RequestBody @Valid CreateGroupForm form, BindingResult result) {

        log.info("form={}", form);

        // API 예외 처리로 교체할 예정.
//        if (result.hasErrors()) {
//            return "groups/createGroupForm";
//        }

        Group group = new Group();
        group.createGroup(form.getName());
        groupService.addGroup(group);

    }

    @GetMapping("/groups")
    public List<GroupsDto> findGroups() {
        return groupRepository.findGroups();
    }

}
