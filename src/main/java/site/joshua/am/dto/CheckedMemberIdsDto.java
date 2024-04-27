package site.joshua.am.dto;

import lombok.Data;

@Data
public class CheckedMemberIdsDto {
    private Long memberId;

    public CheckedMemberIdsDto(Long memberId) {
        this.memberId = memberId;
    }
}
