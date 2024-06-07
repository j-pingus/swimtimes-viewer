package lu.cnw.ranking.controller;

import lombok.AllArgsConstructor;
import lu.cnw.ranking.domain.Time;
import lu.cnw.ranking.repository.TimeRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/athlete", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AthleteController {
    private final TimeRepository timeRepository;

    @GetMapping("{id}/time")
    public List<Time> getTimes(@PathVariable int id) {
        return timeRepository.findByAthleteIdOrderBySecondsAsc(id);
    }

    @GetMapping("{id}/stroke/{stroke}/time")
    public List<Time> getTimes(@PathVariable int id, @PathVariable int stroke) {
        return timeRepository.findByAthleteIdAndStrokeIdOrderBySecondsAsc(id, stroke);
    }

}
