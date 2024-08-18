package site.joshua.am.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.joshua.am.domain.MeetingMinutes;
import site.joshua.am.dto.MeetingMinutesDto;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MeetingMinutesRepository {

    private final EntityManager em;

    public void save(MeetingMinutes minutes) {
        em.persist(minutes);
    }

    public MeetingMinutes findOne(Long id) {
        return em.find(MeetingMinutes.class, id);
    }

    public void delete(MeetingMinutes minutes) {
        em.remove(minutes);
    }

    public List<MeetingMinutesDto> findAllOfMeetingMinutes() {
        return em.createQuery(
                "select new site.joshua.am.dto.MeetingMinutesDto(mm.id, mm.title, mm.content, mm.author.userName, mm.createdAt, mm.updatedAt)" +
                    " from MeetingMinutes mm order by mm.createdAt desc", MeetingMinutesDto.class)
                .getResultList();
    }

    public Optional<MeetingMinutesDto> findOneOfMeetingMinutes(Long minutesId) {
        try {
            MeetingMinutesDto findMeetingMinutes = em.createQuery(
                            "select new site.joshua.am.dto.MeetingMinutesDto(mm.id, mm.title, mm.content, mm.author.userName, mm.createdAt, mm.updatedAt)" +
                                    " from MeetingMinutes mm" +
                                    " where mm.id = :id", MeetingMinutesDto.class)
                    .setParameter("id", minutesId)
                    .getSingleResult();
            return Optional.of(findMeetingMinutes);
        } catch (NoResultException e) {}
        return Optional.empty();
    }
}
