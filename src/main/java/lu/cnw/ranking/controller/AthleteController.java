package lu.cnw.ranking.controller;

import lombok.AllArgsConstructor;
import lu.cnw.ranking.domain.AtheletePoints;
import lu.cnw.ranking.domain.Athlete;
import lu.cnw.ranking.domain.Time;
import lu.cnw.ranking.repository.AthleteRepository;
import lu.cnw.ranking.repository.TimeRepository;
import lu.cnw.ranking.service.ImportService;
import lu.cnw.ranking.utils.DateUtil;
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
    private final ImportService importService;
    private final AthleteRepository athleteRepository;

    @GetMapping("{id}/time")
    public List<Time> getTimes(@PathVariable int id) {
        return timeRepository.findByAthleteIdOrderBySecondsAsc(id);
    }

    @GetMapping("{id}/stroke/{stroke}/time")
    public List<Time> getTimes(@PathVariable int id, @PathVariable int stroke) {
        return timeRepository.findTimes(id, stroke, DateUtil.getYearsOfInterest()[0], DateUtil.getYearsOfInterest()[1]);
    }

    @GetMapping("{id}/stroke/{stroke}/year/{year}/time")
    public List<Time> getTimes(@PathVariable int id, @PathVariable int stroke, @PathVariable String year) {
        // return timeRepository.findByAthleteIdAndStrokeIdOrderBySecondsAsc(id, stroke);
        return timeRepository.findTimes(id, stroke, year, year);
    }

    @GetMapping("{id}/import")
    public void importAthlete(@PathVariable String id) {
        importService.importAthlete(id,false, DateUtil.getYearsOfInterest());
    }

    @GetMapping("{id}/import/{year}")
    public void importAthleteYear(@PathVariable String id, @PathVariable String year) {
        importService.importAthlete(id,true, year);
    }

    @GetMapping("{id}")
    public Athlete getAthlete(@PathVariable int id) {
        return athleteRepository.findById(id).orElseThrow(() -> new Error("Athelete not found"));
    }

    @GetMapping("{id}/points")
    public List<AtheletePoints> getAthletePoints(@PathVariable int id) {
        return timeRepository.getAtheletesPoints(id);
    }
}
