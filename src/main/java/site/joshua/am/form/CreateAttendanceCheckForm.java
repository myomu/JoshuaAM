package site.joshua.am.form;

import lombok.Data;

import java.util.List;

@Data
public class CreateAttendanceCheckForm {

    // 회원의 id를 List 에 담아 createAttendance 로 전달
    private List<Long> memberIds;
    private String year;
    private String month;
    private String day;

}
