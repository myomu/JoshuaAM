package site.joshua.am.domain;

import lombok.Getter;

@Getter
public enum Gender {
    MAN("남자"), WOMAN("여자");

    private final String description;

    Gender(String description) {
        this.description = description;
    }

}
