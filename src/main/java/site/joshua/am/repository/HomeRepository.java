package site.joshua.am.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.joshua.am.dto.HomeAttendanceCountDto;
import site.joshua.am.dto.LongTermAbsentee;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HomeRepository {

    private final EntityManager em;

    public List<HomeAttendanceCountDto> findAttendanceCount() {
        return em.createQuery(
                "select new site.joshua.am.dto.HomeAttendanceCountDto(a.attendanceDate, COUNT(a.id))" +
                        " from AttendanceData ad" +
                        " join ad.attendance a" +
                        " where ad.attendanceStatus = 'ATTENDANCE'" +
                        " group by a.id" +
                        " order by a.attendanceDate desc ", HomeAttendanceCountDto.class)
                .setMaxResults(12)
                .getResultList();
    }

    public List<LongTermAbsentee> findLongTermAbsentee() {
        return em.createQuery(
                "select new site.joshua.am.dto.LongTermAbsentee(ad.member.id, ad.member.name)" +
                        " from AttendanceData ad" +
                        " join ad.attendance a " +
                        " where ad.attendanceStatus = 'ABSENCE'" +
                        " and a.attendanceDate >= (select subA.attendanceDate " +
                        " from AttendanceData subAD " +
                        " join subAD.attendance subA " +
                        " group by subA.id" +
                        " order by subA.attendanceDate desc limit 1 offset 3)" +
                        " group by ad.member.id" +
                        " having count(ad.id) = 4" , LongTermAbsentee.class)
                .getResultList();
    }
}

/*
### 출석과 출석데이터, 회원을 3중 조인하고 서브쿼리로 최근 4주 전 날짜 보다 큰 것을 기준으로
### 결석인 member를 그룹으로 모아서 count를 세었을 때 4 인 member 를 뽑이본다.
### 이렇게하면 최근 4주 동안 결석한 인원을 선별할 수 있다.
select count(ad.attendance_data_id), ad.member_id, m.name from attendance_data ad
join attendance a
on ad.attendance_id = a.attendance_id
join member m
on ad.member_id = m.member_id
where ad.attendance_status = 'ABSENCE'
and a.attendance_date >= (select subA.attendance_date
 from attendance_data subAD
 join attendance subA
 on subAD.attendance_id = subA.attendance_id
 group by subAD.attendance_id
 order by subA.attendance_date desc limit 1 offset 3)
group by ad.member_id
having count(ad.attendance_data_id) = 4;
*/
