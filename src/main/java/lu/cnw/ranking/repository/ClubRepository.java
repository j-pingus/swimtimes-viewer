package lu.cnw.ranking.repository;

import lu.cnw.ranking.domain.Athlete;
import lu.cnw.ranking.domain.Club;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends CrudRepository<Club,Integer> {
    Optional< Club> findByName(String name);

    @Override
    List<Club> findAll();
}
