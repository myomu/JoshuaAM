package site.joshua.am.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.joshua.am.domain.Member;
import site.joshua.am.dto.AttendanceCheckDto;
import site.joshua.am.dto.MembersDto;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m order by m.name", Member.class)
                .getResultList();
    }

    public List<Member> findAllByGroupId(Long groupId) {
        return em.createQuery("select m from Member m where m.group.id = :groupId", Member.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<MembersDto> findMembers() {
        return em.createQuery(
                        "select new site.joshua.am.dto.MembersDto(m.id, m.name, m.age, m.gender, g.name, m.memberStatus)" +
                                " from Member m" +
                                " join m.group g", MembersDto.class)
                .getResultList();
    }

}
