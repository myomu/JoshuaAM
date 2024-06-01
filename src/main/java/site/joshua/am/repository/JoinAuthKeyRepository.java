package site.joshua.am.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.joshua.am.domain.JoinAuthKey;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JoinAuthKeyRepository {

    private final EntityManager em;

    public List<JoinAuthKey> findKeys() {
        return em.createQuery("select jk from JoinAuthKey jk", JoinAuthKey.class).getResultList();
    }

}
