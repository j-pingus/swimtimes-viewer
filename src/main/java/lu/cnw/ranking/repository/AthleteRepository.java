package lu.cnw.ranking.repository;

import lu.cnw.ranking.domain.Athlete;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AthleteRepository extends CrudRepository<Athlete, Integer> {
    Optional<Athlete> findBySwimRankingId(String id);

    List<Athlete> findByClubIdOrderByName(int id);
}
