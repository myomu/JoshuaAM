package site.joshua.am.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.joshua.am.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public Optional<User> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(u -> Optional.ofNullable(u.getUserLoginId()).orElse("").equals(loginId))
                .findFirst();
    }

//    public User findByLoginId(String loginId) {
//        return em.createQuery("select u from User u " +
//                "where u.userLoginId = :loginId", User.class)
//                .setParameter("loginId", loginId)
//                .getSingleResult();
//    }

}
