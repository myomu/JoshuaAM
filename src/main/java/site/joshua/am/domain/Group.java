package site.joshua.am.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "member_group")
public class Group {

    @Id @GeneratedValue
    @Column(name = "group_id")
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "group")
    private List<Member> members = new ArrayList<>();

    public void createGroup(String name) {
        this.name = name;
    }

    public void editGroup(String name) {
        this.name = name;
    }

    public void setGroupId(Long groupId) {
        this.id = groupId;
    }
}
