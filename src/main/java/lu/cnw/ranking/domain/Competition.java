package lu.cnw.ranking.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Competition {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CompetitionSequence")
    @SequenceGenerator(name = "CompetitionSequence")
    @Id
    Integer id;
    String name;
    String course;
    String date;
    String swimRankingId;
}
