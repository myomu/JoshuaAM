package site.joshua.am.apicontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.joshua.am.S3Service;
import site.joshua.am.dto.MeetingMinutesDto;
import site.joshua.am.form.CreateMeetingMinutesForm;
import site.joshua.am.form.DeleteMeetingMinutesForm;
import site.joshua.am.form.EditMeetingMinutesForm;
import site.joshua.am.repository.MeetingMinutesRepository;
import site.joshua.am.service.MeetingMinutesService;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/minutes")
@Slf4j
public class MeetingMinutesApiController {

    private final MeetingMinutesService meetingMinutesService;
    private final MeetingMinutesRepository meetingMinutesRepository;

    private final S3Client s3Client;
    private final S3Service s3Service;

    /**
     * 회의록 생성
     */
    @PostMapping("/create")
    public ResponseEntity<?> createMinutes(@RequestBody CreateMeetingMinutesForm form) throws Exception {
        log.info("Create minutes: {}", form.toString());
        meetingMinutesService.addMeetingMinutes(form);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    /**
     * 회의록 작성 시 이미지 업로드
     */
    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("Upload image: {}", file.toString());
        try {
            String fileUrl = s3Service.uploadFile(file);
            log.info("url: {}", fileUrl);
            return new ResponseEntity<>(new UploadResponse(fileUrl), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 업로드 파일 껍데기
    static class UploadResponse {
        public String url;

        public UploadResponse(String url) {
            this.url = url;
        }
    }

    /**
     * 회의록 목록 화면 요청
     */
    @GetMapping("")
    public ResponseEntity<?> findAllMeetingMinutes() throws Exception {
        List<MeetingMinutesDto> meetingMinutes = meetingMinutesRepository.findAllOfMeetingMinutes();
        if (!(meetingMinutes == null)) {
            return new ResponseEntity<>(meetingMinutes, HttpStatus.OK);
        }

        return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
    }

    /**
     * 회의록 화면 요청 (디테일 화면) 및 수정 화면 요청
     */
    @GetMapping("/{minutesId}")
    public ResponseEntity<?> findMeetingMinutesById(@PathVariable Long minutesId) {
        Optional<MeetingMinutesDto> oneOfMeetingMinutes = meetingMinutesRepository.findOneOfMeetingMinutes(minutesId);

        if (oneOfMeetingMinutes.isPresent()) {
            return new ResponseEntity<>(oneOfMeetingMinutes.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 회의록 수정
     */
    @PostMapping("/edit/{minutesId}")
    public ResponseEntity<?> editMeetingMinutes(@RequestBody @Valid EditMeetingMinutesForm form, @PathVariable Long minutesId) {
        log.info("minutesId={}", minutesId);
        try {
            meetingMinutesService.editMeetingMinutes(minutesId, form);
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회의록 삭제
     */
    @PostMapping("/delete")
    public void deleteMeetingMinutes(@RequestBody @Valid DeleteMeetingMinutesForm form) {
        log.info("form={}", form);
        List<Long> meetingMinutesIds = form.getMeetingMinutesIds();
        meetingMinutesService.deleteMeetingMinutes(meetingMinutesIds);
    }

}
