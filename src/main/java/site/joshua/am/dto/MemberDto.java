package site.joshua.am.dto;

import lombok.Data;
import site.joshua.am.domain.Gender;
import site.joshua.am.domain.MemberStatus;

@Data
public class MemberDto {

    private Long memberId;
    private String name;
    private int age;
    private Gender gender;
    private Long groupId;
    private String groupName;
    private MemberStatus memberStatus;

    public MemberDto(Long memberId, String name, int age, Gender gender, Long groupId, String groupName, MemberStatus memberStatus) {
        this.memberId = memberId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.groupId = groupId;
        this.groupName = groupName;
        this.memberStatus = memberStatus;
    }
}
