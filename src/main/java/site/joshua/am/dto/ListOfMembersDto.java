package site.joshua.am.dto;

import lombok.Data;
import site.joshua.am.domain.Member;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListOfMembersDto {

    private Long groupId;
    private String groupName;
    private List<AttendanceMembersDto> members = new ArrayList<>();

    public ListOfMembersDto() {
    }

    public void addList(AttendanceMembersDto member) {
        this.members.add(member);
    }
}
