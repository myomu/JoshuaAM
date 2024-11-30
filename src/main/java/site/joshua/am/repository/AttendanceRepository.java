package site.joshua.am.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import site.joshua.am.domain.Attendance;
import site.joshua.am.dto.AttendanceDto;
import site.joshua.am.dto.AttendanceMembersDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AttendanceRepository {

    private final EntityManager em;

    public void save(Attendance attendance) {
        em.persist(attendance);
    }

    public Attendance findOne(Long id) {
        return em.find(Attendance.class, id);
    }

    public List<Attendance> findAll(Pageable pageable) {
        return em.createQuery("select a from Attendance a" +
                        " order by a.attendanceDate desc", Attendance.class)
                .setFirstResult((int) pageable.getOffset()) // OFFSET 설정
                .setMaxResults(pageable.getPageSize()) // LIMIT 설정
                .getResultList();
    }

    public List<Attendance> findAttendanceBySort(Pageable pageable) {

        Optional<Sort.Order> sort = pageable.getSort().stream().findFirst();
        String jpql = "select a from Attendance a" +
                " order by a.attendanceDate desc";

        if (sort.isPresent()) {
            Sort.Order order = sort.get();
            String field = order.getProperty();
            Sort.Direction direction = order.getDirection();

            log.info("field: {}, direction: {}", field, direction);


            if (field.equals("totalMember")) {
                jpql = "SELECT a FROM Attendance a " +
                        "LEFT JOIN a.attendanceDataList ad " +
                        "WHERE ad.attendanceStatus = 'ATTENDANCE'" +
                        "GROUP BY a " +
                        "ORDER BY COUNT(ad) " + direction;
            } else if (field.equals("attendanceDate")) {
                jpql = "SELECT a FROM Attendance a " +
                        "ORDER BY a.attendanceDate " + direction;
            }

            return em.createQuery(jpql, Attendance.class)
                    .setFirstResult((int) pageable.getOffset()) // OFFSET 설정
                    .setMaxResults(pageable.getPageSize()) // LIMIT 설정
                    .getResultList();
        }

        return em.createQuery(jpql, Attendance.class)
                .setFirstResult((int) pageable.getOffset()) // OFFSET 설정
                .setMaxResults(pageable.getPageSize()) // LIMIT 설정
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

    public long countAll() {
        return em.createQuery("select count(a) from Attendance a", Long.class)
                .getSingleResult();
    }
}
