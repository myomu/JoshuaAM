package site.joshua.am.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @NotNull //Integer 타입의 경우에는 NotNull을 사용하고 String 타입의 경우에는 NotEmpty를 사용한다.
    private int dateOfBirth; // age(나이) -> dateOfBirth(생년월일) 로 수정

    @NotNull //Enum 타입의 경우도 NotEmpty를 사용하지 않고 NotNull을 사용해야한다.
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void createMember(String name, int dateOfBirth, Gender gender, Group group, MemberStatus memberStatus) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.group = group;
        this.memberStatus = memberStatus;
    }

    public void editMember(String name, int dateOfBirth, Gender gender, Group group) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.group = group;
    }

    public void setMemberId(Long id) {
        this.id = id;
    }

}
