package site.joshua.am.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class MeetingMinutes {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "minutes_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "meetingMinutes", cascade = CascadeType.REMOVE)
    private List<MeetingMinutesFile> meetingMinutesFiles = new ArrayList<>();

    public void createMeetingMinutes(String title, String content, User author, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void editMeetingMinutes(String title, String content, LocalDateTime updatedAt) {
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
    }
}
