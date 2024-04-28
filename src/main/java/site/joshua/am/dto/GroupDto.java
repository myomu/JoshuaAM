package site.joshua.am.dto;

import lombok.Data;

@Data
public class GroupDto {
    private Long groupId;
    private String groupName;

    public GroupDto(Long groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
