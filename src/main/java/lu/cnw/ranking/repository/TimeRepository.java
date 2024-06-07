package lu.cnw.ranking.repository;

import lu.cnw.ranking.domain.Athlete;
import lu.cnw.ranking.domain.Competition;
import lu.cnw.ranking.domain.Stroke;
import lu.cnw.ranking.domain.Time;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TimeRepository extends CrudRepository<Time,Integer> {
    Optional<Time> findByAthleteAndStrokeAndCompetition(Athlete athlete, Stroke stroke, Competition competition);
    List<Time> findByAthleteIdAndStrokeIdOrderBySecondsAsc(int athlete, int stroke);
    List<Time> findByAthleteIdOrderBySecondsAsc(int athlete);
}
