package site.joshua.am.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    @NotNull //Integer 타입의 경우에는 NotNull을 사용하고 String 타입의 경우에는 NotEmpty를 사용한다.
    @Column(nullable = false)
    private LocalDateTime birthdate; // age(나이) -> birthdate(생년월일) 로 수정

    @NotNull //Enum 타입의 경우도 NotEmpty를 사용하지 않고 NotNull을 사용해야한다.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus memberStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    public void createMember(String name, LocalDateTime birthdate, Gender gender, Group group, MemberStatus memberStatus) {
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.group = group;
        this.memberStatus = memberStatus;
    }

    public void editMember(String name, LocalDateTime birthdate, Gender gender, Group group) {
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.group = group;
    }

    public void nullifyGroupId(String name, LocalDateTime birthdate, Gender gender) {
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.group = null;
    }

    public void setMemberId(Long id) {
        this.id = id;
    }

    public void setNullGroupFK() {
        this.group = null;
    }

    public void changeMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }

    // birthdate 가 null 일 경우 기본값을 설정해준다.
    private static final LocalDateTime DEFAULT_BIRTHDATE = LocalDateTime.of(1999, 1, 1, 0, 0);//LocalDate.of(1999, 1, 1).atStartOfDay();

    @PrePersist
    @PreUpdate
    private void ensureBirthdate() {
        if (this.birthdate == null) {
            this.birthdate = DEFAULT_BIRTHDATE;
        }
    }
}
