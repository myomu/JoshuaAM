package site.joshua.am.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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

    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Attendance> attendances = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void createMember(String name, int age, Gender gender, Group group, User user, MemberStatus memberStatus) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.group = group;
        this.user = user;
        this.memberStatus = memberStatus;
    }

    public void editMember(String name, int age, Gender gender, Group group) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.group = group;
    }

    public void setMemberId(Long id) {
        this.id = id;
    }

}
