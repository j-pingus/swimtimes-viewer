package lu.cnw.ranking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.cnw.ranking.domain.*;
import lu.cnw.ranking.repository.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
@Slf4j
public class ImportService {
    private final SwimRankingBrowserService swimRankingBrowserService;
    private final AthleteRepository athleteRepository;
    private final CompetitionRepository competitionRepository;
    private final StrokeRepository strokeRepository;
    private final ClubRepository clubRepository;
    private final TimeRepository timeRepository;

    public void importAthlete(String id) throws IOException {
        var athleteDetails = swimRankingBrowserService.getAthleteDetails(id);
        var foundAthlete = athleteRepository.findBySwimRankingId(id);
        var foundClub = clubRepository.findByName(athleteDetails.club());

        Club club = foundClub.orElseGet(() ->
                clubRepository.save(new Club()
                        .setName(athleteDetails.club())
                ));
        Athlete athlete = foundAthlete.orElseGet(() ->
                athleteRepository.save(new Athlete()
                        .setSwimRankingId(id)
                        .setName(athleteDetails.name())
                        .setClub(club)
                ));
        athleteDetails.competitionList().forEach(competition -> {
            importCompetition(athlete, competition);
        });
    }

    private void importCompetition(Athlete athlete, SwimRankingBrowserService.Competition comp) {
        var foundCompetition = competitionRepository.findBySwimRankingId(comp.swimRankingId());
        if (foundCompetition.isPresent()) return;
        Competition competition =
                competitionRepository.save(new Competition()
                        .setName(comp.city())
                        .setDate(comp.date())
                        .setCourse(comp.course())
                        .setSwimRankingId(comp.swimRankingId())
                );
        try {
            swimRankingBrowserService.getAthleteTimes(athlete.getName(), athlete.getSwimRankingId(), comp.swimRankingId(), comp.clubId()).forEach(
                    time -> {
                        var foundStroke = strokeRepository.findByName(time.stroke());
                        var stroke = foundStroke.orElseGet(
                                () -> strokeRepository.save(new Stroke().setName(time.stroke()).setDistance(time.distance()))
                        );
                        var foundTime = timeRepository.findByAthleteAndStrokeAndCompetition(athlete, stroke, competition);
                        if (foundTime.isEmpty()) {
                            timeRepository.save(new Time()
                                    .setCompetition(competition)
                                    .setStroke(stroke)
                                    .setAthlete(athlete)
                                    .setTime(time.time())
                                    .setSeconds(time.seconds())
                            );
                        }
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
