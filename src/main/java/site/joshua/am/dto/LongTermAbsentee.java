package site.joshua.am.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LongTermAbsentee {

    private Long memberId;
    private String memberName;

    public LongTermAbsentee(Long memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
    }
}
