package lu.cnw.ranking.repository;

import lu.cnw.ranking.domain.Athlete;
import lu.cnw.ranking.domain.Competition;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CompetitionRepository extends CrudRepository<Competition,Integer> {
    Optional<Competition> findBySwimRankingId(String id);

}
