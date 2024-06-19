package site.joshua.am.dto;

import lombok.Data;
import site.joshua.am.domain.Gender;
import site.joshua.am.domain.MemberStatus;

import java.time.LocalDateTime;

@Data
public class MemberListDto {

    private Long memberId;
    private String name;
    private LocalDateTime birthdate;
    private Gender gender;
    private Long groupId;
    private String groupName;
    private MemberStatus memberStatus;
    private Double attendanceRate = 0.0;

    public MemberListDto(Long memberId, String name, LocalDateTime birthdate, Gender gender, Long groupId, String groupName, MemberStatus memberStatus) {
        this.memberId = memberId;
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.groupId = groupId;
        this.groupName = groupName;
        this.memberStatus = memberStatus;
    }

}
