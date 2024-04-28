package site.joshua.am.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.joshua.am.domain.Group;
import site.joshua.am.dto.GroupDto;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GroupRepository {

    private final EntityManager em;

    public void save(Group group) {
        em.persist(group);
    }

    public Group findOne(Long id) {
        return em.find(Group.class, id);
    }

    public List<Group> findAll() {
        return em.createQuery("select g from Group g", Group.class)
                .getResultList();
    }

    public void delete(Group group) {
        em.remove(group);
    }

    public List<GroupDto> findGroups() {
        return em.createQuery(
                        "select new site.joshua.am.dto.GroupDto(g.id, g.name)" +
                                " from Group g", GroupDto.class)
                .getResultList();
    }

    public GroupDto findGroup(Long groupId) {
        return em.createQuery(
                "select new site.joshua.am.dto.GroupDto(g.id, g.name)" +
                        " from Group g" +
                        " where g.id =: groupId", GroupDto.class)
                .setParameter("groupId", groupId)
                .getSingleResult();
    }
}
