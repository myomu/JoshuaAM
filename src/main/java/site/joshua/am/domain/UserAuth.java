package site.joshua.am.domain;

import lombok.Getter;

@Getter
public enum UserAuth {
    ADMIN("관리자"), USER("사용자");

    private final String description;

    UserAuth(String description) {
        this.description = description;
    }
}
