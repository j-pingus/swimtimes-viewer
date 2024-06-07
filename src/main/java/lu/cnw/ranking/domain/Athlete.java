package lu.cnw.ranking.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Athlete {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AthleteSequence")
    @SequenceGenerator(name = "AthleteSequence")
    @Id
    Integer id;
    String name;
    String swimRankingId;
    @ManyToOne
    Club club;
}
