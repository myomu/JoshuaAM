package site.joshua.am.form;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeleteAttendanceForm {
    private List<Long> attendanceIds = new ArrayList<>();
}
