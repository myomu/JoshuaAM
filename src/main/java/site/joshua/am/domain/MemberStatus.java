package site.joshua.am.domain;

import lombok.Getter;

@Getter
public enum MemberStatus {
    MEMBER("회원"), NON_MEMBER("비회원");

    private final String description;

    MemberStatus(String description) {
        this.description = description;
    }
}
