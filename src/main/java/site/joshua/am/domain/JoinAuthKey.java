package site.joshua.am.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class JoinAuthKey {

    @Id @GeneratedValue
    private Long id;

    private String authKey;
}
