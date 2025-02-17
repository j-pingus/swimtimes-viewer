package lu.cnw.ranking.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Time {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TimeSequence")
    @SequenceGenerator(name = "TimeSequence")
    @Id
    Integer id;
    @ManyToOne
    Athlete athlete;
    @ManyToOne
    Competition competition;
    @ManyToOne
    Stroke stroke;
    String time;
    Double seconds;
    Long points;
}
