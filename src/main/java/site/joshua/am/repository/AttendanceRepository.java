package site.joshua.am.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.joshua.am.domain.Attendance;
import site.joshua.am.dto.AttendanceMembersDto;

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
        return em.createQuery("select a from Attendance a" +
                        " order by a.attendanceDate desc", Attendance.class)
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


    public List<AttendanceMembersDto> findListOfMembers() {
        return em.createQuery(
                        "select new site.joshua.am.dto.AttendanceMembersDto(m.id, m.name, g.id)" +
                                " from Member m" +
                                " join m.group g" +
                                " where m.memberStatus = 'MEMBER'", AttendanceMembersDto.class)
                .getResultList();
    }

    public Long countAttendanceByAttendanceDate(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return em.createQuery(
                            "select count(*)" +
                                    " from Attendance a", Long.class)
                    .getSingleResult();
        } else {
            return em.createQuery(
                            "select count(*)" +
                                    " from Attendance a" +
                                    " where a.attendanceDate between :startDate and :endDate", Long.class)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getSingleResult();
        }
    }

}
