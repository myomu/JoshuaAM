package site.joshua.am.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.joshua.am.domain.AttendanceData;
import site.joshua.am.dto.AttendanceDataDto;
import site.joshua.am.dto.CheckedMemberIdsDto;
import site.joshua.am.dto.MembersDto;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttendanceDataRepository {

    private final EntityManager em;

    public void save(AttendanceData attendanceData) {
        em.persist(attendanceData);
    }

    public List<AttendanceDataDto> findAttendanceData(Long attendanceId) {
        return em.createQuery(
                "select new site.joshua.am.dto.AttendanceDataDto(ad.id, ad.member.id, ad.member.name, ad.attendanceStatus)" +
                        " from AttendanceData ad" +
                        " join ad.attendance a" +
                        " where ad.attendanceStatus = 'ATTENDANCE'" +
                        " and ad.attendance.id = :attendanceId" +
                        " order by ad.member.name", AttendanceDataDto.class)
                .setParameter("attendanceId", attendanceId)
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

    public List<MembersDto> findMembers() {
        return em.createQuery(
                        "select new site.joshua.am.dto.MembersDto(m.id, m.name, m.age, m.gender, g.name, m.memberStatus)" +
                                " from Member m" +
                                " join m.group g", MembersDto.class)
                .getResultList();
    }


}
