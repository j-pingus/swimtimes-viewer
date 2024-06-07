package lu.cnw.ranking.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Club {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ClubSequence")
    @SequenceGenerator(name = "ClubSequence")
    @Id
    Integer id;
    String name;

}
