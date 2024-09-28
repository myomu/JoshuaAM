package site.joshua.am.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class JoinAuthKey {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authKey;
}
