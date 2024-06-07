package lu.cnw.ranking.controller;

import lombok.AllArgsConstructor;
import lu.cnw.ranking.domain.Athlete;
import lu.cnw.ranking.domain.Club;
import lu.cnw.ranking.domain.Stroke;
import lu.cnw.ranking.repository.AthleteRepository;
import lu.cnw.ranking.repository.ClubRepository;
import lu.cnw.ranking.repository.StrokeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ReferencesController {
    private final ClubRepository clubRepository;
    private final AthleteRepository athleteRepository;
    private final StrokeRepository strokeRepository;

    @GetMapping("club")
    List<Club> getClubs() {
        return clubRepository.findAll();
    }

    @GetMapping("club/{id}/athletes")
    List<Athlete> getAthletes(@PathVariable int id) {
        return athleteRepository.findByClubId(id);
    }

    @GetMapping("stroke")
    List<Stroke> getStrokes() {
        return strokeRepository.findAll(Sort.by("distance","name"));
    }

}
