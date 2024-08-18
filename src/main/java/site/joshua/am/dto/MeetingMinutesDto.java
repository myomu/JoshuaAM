package site.joshua.am.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingMinutesDto {

    private Long meetingMinutesId;
    private String title;
    private String content;
    private String author; // Id 를 가져오지 말고 이름을 바로 받아오게끔 함
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MeetingMinutesDto(Long meetingMinutesId, String title, String content, String author, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.meetingMinutesId = meetingMinutesId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
