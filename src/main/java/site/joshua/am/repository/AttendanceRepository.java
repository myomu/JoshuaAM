package site.joshua.am.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.joshua.am.domain.Attendance;
import site.joshua.am.dto.AttendanceCheckDto;
import site.joshua.am.dto.AttendanceDto;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttendanceRepository {

    private final EntityManager em;

    public void save(Attendance attendance) {
        em.persist(attendance);
    }

    public Attendance findOne(Long id) {
        return em.find(Attendance.class, id);
    }

    public List<Attendance> findAll() {
        return em.createQuery("select a from Attendance a order by a.member.name", Attendance.class)
                .getResultList();
    }

    public List<LocalDateTime> findNoDuplicateDate() {
        return em.createQuery("select a.attendanceDate from Attendance a group by a.attendanceDate order by a.attendanceDate desc", LocalDateTime.class)
                .getResultList();
    }

    public List<Attendance> findAllByDateTime(LocalDateTime dateTime) {
        return em.createQuery("select a from Attendance a where a.attendanceDate = :dateTime", Attendance.class)
                .setParameter("dateTime", dateTime)
                .getResultList();
    }

    public void delete(Attendance attendance) {
        em.remove(attendance);
    }

    public List<AttendanceDto> findAttendances() {
        return em.createQuery(
                "select new site.joshua.am.dto.AttendanceDto(a.id, a.attendanceDate, a.attendanceStatus, m.id)" +
                        " from Attendance a" +
                        " join a.member m", AttendanceDto.class)
                .getResultList();
    }

    public List<AttendanceCheckDto> findAttendanceCheckList() {
        return em.createQuery(
                "select new site.joshua.am.dto.AttendanceCheckDto(m.id, m.name, g.id, g.name)" +
                        " from Member m" +
                        " join m.group g", AttendanceCheckDto.class)
                .getResultList();
    }

}
