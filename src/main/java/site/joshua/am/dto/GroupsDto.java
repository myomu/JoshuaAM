package site.joshua.am.dto;

import lombok.Data;

@Data
public class GroupsDto {
    private Long id;
    private String name;

    public GroupsDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
