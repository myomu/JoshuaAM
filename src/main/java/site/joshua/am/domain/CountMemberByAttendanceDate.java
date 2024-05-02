package site.joshua.am.domain;

import lombok.Data;

@Data
public class CountMemberByAttendanceDate {
    private Long memberId;
    private long memberAttendanceCount;

    public CountMemberByAttendanceDate(Long memberId, long memberAttendanceCount) {
        this.memberId = memberId;
        this.memberAttendanceCount = memberAttendanceCount;
    }
}
