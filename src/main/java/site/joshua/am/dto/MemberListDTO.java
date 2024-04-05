package site.joshua.am.dto;

import lombok.Getter;
import lombok.Setter;
import site.joshua.am.domain.Member;

@Getter @Setter
public class MemberListDTO {

    private Member member;
    private Double attendanceRate;
    private int noa; //numberOfAttendance

    public MemberListDTO(Member member, Double attendanceRate, int noa) {
        this.member = member;
        this.attendanceRate = attendanceRate;
        this.noa = noa;
    }

}
