package site.joshua.am.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.joshua.am.domain.MeetingMinutes;
import site.joshua.am.domain.User;
import site.joshua.am.dto.MeetingMinutesDto;
import site.joshua.am.form.CreateMeetingMinutesForm;
import site.joshua.am.form.EditMeetingMinutesForm;
import site.joshua.am.repository.MeetingMinutesRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingMinutesService {

    private final UserService userService;
    private final MeetingMinutesRepository meetingMinutesRepository;

    /**
     * 회의록 추가
     */
    @Transactional
    public Long addMeetingMinutes(CreateMeetingMinutesForm form) throws Exception {
        User author = userService.findUser(form.getUserId());

        // 생성 시에는 업데이트 시간을 생성 시간과 동일하게 설정
        LocalDateTime updatedAt = form.getCreatedAt();

        MeetingMinutes meetingMinutes = new MeetingMinutes();
        meetingMinutes.createMeetingMinutes(form.getTitle(), form.getContent(), author, form.getCreatedAt(), updatedAt);

        meetingMinutesRepository.save(meetingMinutes);

        return meetingMinutes.getId();
    }

    /**
     * 회의록 수정
     */
    @Transactional
    public void editMeetingMinutes(Long meetingMinutesId, EditMeetingMinutesForm form) throws Exception {
        MeetingMinutes findMeetingMinutes = meetingMinutesRepository.findOne(meetingMinutesId);

        String title = form.getTitle();
        String content = form.getContent();
        LocalDateTime updatedAt = form.getUpdatedAt();

        findMeetingMinutes.editMeetingMinutes(title, content, updatedAt);
    }

    /**
     * DB 에서 MeetingMinutes 완전 삭제
     */
    @Transactional
    public void deleteMeetingMinutes(List<Long> minutesIds) {
        for (Long minutesId : minutesIds) {
            Optional<MeetingMinutesDto> findMeetingMinutes = meetingMinutesRepository.findOneOfMeetingMinutes(minutesId);
            if (findMeetingMinutes.isPresent()) {
                Long meetingMinutesId = findMeetingMinutes.get().getMeetingMinutesId();
                MeetingMinutes findOne = meetingMinutesRepository.findOne(meetingMinutesId);
                meetingMinutesRepository.delete(findOne);
            }

        }
    }
}
