package site.joshua.am.dto;

import lombok.Data;

@Data
public class AttendanceCheckDto {

    private Long memberId;
    private String name;
    private Long groupId;
    private String groupName;

    public AttendanceCheckDto(Long memberId, String name, Long groupId, String groupName) {
        this.memberId = memberId;
        this.name = name;
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
