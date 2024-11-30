package site.joshua.am.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.joshua.am.domain.AttendanceData;
import site.joshua.am.dto.AttendanceDataDto;
import site.joshua.am.dto.CheckedMemberIdsDto;
import site.joshua.am.domain.CountMemberByAttendanceDate;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttendanceDataRepository {

    private final EntityManager em;

    public void save(AttendanceData attendanceData) {
        em.persist(attendanceData);
    }

    public List<AttendanceDataDto> findAttendanceDataById(Long attendanceId) {
        return em.createQuery(
                "select new site.joshua.am.dto.AttendanceDataDto(ad.id, ad.member.id, ad.member.name, ad.attendanceStatus, ad.attendance.id)" +
                        " from AttendanceData ad" +
                        " join ad.attendance a" +
                        " where ad.attendanceStatus = 'ATTENDANCE'" +
                        " and ad.attendance.id = :attendanceId" +
                        " order by ad.member.name", AttendanceDataDto.class)
                .setParameter("attendanceId", attendanceId)
                .getResultList();
    }

    public List<AttendanceDataDto> findAllAttendanceData() {
        return em.createQuery(
                "select new site.joshua.am.dto.AttendanceDataDto(ad.id, ad.member.id, ad.member.name, ad.attendanceStatus, a.id)" +
                        " from AttendanceData ad" +
                        " join ad.attendance a" +
                        " where ad.attendanceStatus = 'ATTENDANCE'" +
                        " order by a.attendanceDate desc, ad.member.name", AttendanceDataDto.class)
                .getResultList();
    }

    public List<CheckedMemberIdsDto> findSelectedMembers(Long attendanceId) {
        return em.createQuery(
                "select new site.joshua.am.dto.CheckedMemberIdsDto(ad.member.id)" +
                        " from AttendanceData ad" +
                        " where ad.attendance.id = :attendanceId" +
                        " and ad.attendanceStatus = 'ATTENDANCE'" +
                        " order by ad.id", CheckedMemberIdsDto.class)
                .setParameter("attendanceId", attendanceId)
                .getResultList();
    }

//    public List<MemberDto> findMembers() {
//        return em.createQuery(
//                        "select new site.joshua.am.dto.MemberDto(m.id, m.name, m.age, m.gender, g.name, m.memberStatus)" +
//                                " from Member m" +
//                                " join m.group g", MemberDto.class)
//                .getResultList();
//    }

    public List<CountMemberByAttendanceDate> countMemberAttendanceByAttendanceDate(LocalDateTime startDate, LocalDateTime endDate) {

        if (startDate == null || endDate == null) {
            return em.createQuery(
                            "select new site.joshua.am.domain.CountMemberByAttendanceDate(ad.member.id, COUNT(ad.member.id))" +
                                    " from AttendanceData ad" +
                                    " join ad.attendance a" +
                                    " where ad.attendanceStatus = 'ATTENDANCE'" +
                                    " group by ad.member.id" +
                                    " order by ad.member.id", CountMemberByAttendanceDate.class)
                    .getResultList();
        } else {
            return em.createQuery(
                            "select new site.joshua.am.domain.CountMemberByAttendanceDate(ad.member.id, COUNT(ad.member.id))" +
                                    " from AttendanceData ad" +
                                    " join ad.attendance a" +
                                    " where a.attendanceDate between :startDate and :endDate" +
                                    " and ad.attendanceStatus = 'ATTENDANCE'" +
                                    " group by ad.member.id" +
                                    " order by ad.member.id", CountMemberByAttendanceDate.class)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
        }
    }

}
