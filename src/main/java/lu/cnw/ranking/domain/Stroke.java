package lu.cnw.ranking.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Stroke {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "StrokeSequence")
    @SequenceGenerator(name = "StrokeSequence")
    @Id
    Integer id;
    String name;
    int distance;
}
