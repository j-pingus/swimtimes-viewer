package lu.cnw.ranking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.cnw.ranking.domain.*;
import lu.cnw.ranking.repository.*;
import lu.cnw.ranking.utils.DateUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

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
        Optional<SwimRankingBrowserService.AthleteDetails> athleteDetails = this.swimRankingBrowserService.getAthleteDetails(id);
        if (athleteDetails.isPresent()) {
            Optional<Athlete> foundAthlete = this.athleteRepository.findBySwimRankingId(id);
            Optional<Club> foundClub = this.clubRepository.findByName(athleteDetails.get().club());
            Club club = foundClub.orElseGet(() -> this.clubRepository.save((new Club()).setName(athleteDetails.get().club())));
            logger.info("Club:{}", club);
            Athlete athlete = foundAthlete.orElseGet(() -> this.athleteRepository.save((new Athlete()).setSwimRankingId(id).setName(athleteDetails.get().name()).setClub(club)));
            logger.info("Athlete:{}", athlete);
            athleteDetails.get().competitionList().forEach((competition) -> this.importCompetition(athlete, competition));
        }
    }

    private void importCompetition(Athlete athlete, SwimRankingBrowserService.Competition comp) {
        Optional<Competition> foundCompetition = this.competitionRepository.findBySwimRankingId(comp.swimRankingId());
        if (DateUtil.isYearOfInterest(comp.date())) {
            Competition competition = foundCompetition.orElseGet(() -> this.competitionRepository.save((new Competition()).setName(comp.city()).setDate(comp.date()).setCourse(comp.course()).setSwimRankingId(comp.swimRankingId())));
            logger.info("Competition:{}", competition);

            try {
                this.swimRankingBrowserService.getAthleteTimes(athlete.getName(), comp.swimRankingId(), comp.clubId()).forEach((time) -> {
                    Optional<Stroke> foundStroke = this.strokeRepository.findByName(time.stroke());
                    Stroke stroke = foundStroke.orElseGet(() -> this.strokeRepository.save((new Stroke()).setName(time.stroke()).setDistance(time.distance())));
                    Optional<Time> foundTime = this.timeRepository.findByAthleteAndStrokeAndCompetition(athlete, stroke, competition);
                    if (foundTime.isEmpty()) {
                        this.timeRepository.save((new Time()).setCompetition(competition).setStroke(stroke).setAthlete(athlete).setTime(time.time()).setSeconds(time.seconds()));
                    }

                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}