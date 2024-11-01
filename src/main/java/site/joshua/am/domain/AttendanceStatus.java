package site.joshua.am.domain;

import lombok.Getter;

@Getter
public enum AttendanceStatus {
    ATTENDANCE("출석"), ABSENCE("결석");

    private final String description;

    AttendanceStatus(String description) {
        this.description = description;
    }

}
