package site.joshua.am.dto;

import lombok.Data;

@Data
public class AttendanceMembersDto {

    private Long memberId;
    private String name;
    private Long groupId;

    public AttendanceMembersDto(Long memberId, String name, Long groupId) {
        this.memberId = memberId;
        this.name = name;
        this.groupId = groupId;
    }
}
