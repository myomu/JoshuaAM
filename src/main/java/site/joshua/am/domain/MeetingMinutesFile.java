package site.joshua.am.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class MeetingMinutesFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "minutes_file_id")
    private Long id;

    private String uploadFileName;

    private String storeFileName;

    private LocalDateTime uploadFileDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "minutes_id", nullable = false)
    private MeetingMinutes meetingMinutes;

    public void setMinutesFile(String uploadFileName, String storeFileName, LocalDateTime uploadFileDate) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.uploadFileDate = uploadFileDate;
    }

}
