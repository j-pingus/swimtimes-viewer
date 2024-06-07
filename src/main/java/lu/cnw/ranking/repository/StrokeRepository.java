package lu.cnw.ranking.repository;

import lu.cnw.ranking.domain.Stroke;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StrokeRepository extends CrudRepository<Stroke, Integer> {
    Optional<Stroke> findByName(String name);

    List<Stroke> findAll(Sort sort);
}
