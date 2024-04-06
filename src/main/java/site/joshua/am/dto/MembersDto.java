package site.joshua.am.dto;

import lombok.Data;
import site.joshua.am.domain.Gender;
import site.joshua.am.domain.MemberStatus;

@Data
public class MembersDto {

    private Long memberId;
    private String name;
    private int age;
    private String gender;
    private String groupName;
    private String memberStatus;

    public MembersDto(Long memberId, String name, int age, Gender gender, String groupName, MemberStatus memberStatus) {
        this.memberId = memberId;
        this.name = name;
        this.age = age;
        this.gender = gender.getDescription();
        this.groupName = groupName;
        this.memberStatus = memberStatus.getDescription();
    }
}
