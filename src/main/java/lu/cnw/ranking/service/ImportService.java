package lu.cnw.ranking.service;

import jakarta.transaction.Transactional;
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

    @Transactional
    public void importAthlete(String id, boolean clearCache, String... years) {
        Optional<SwimRankingBrowserService.AthleteDetails> athleteDetails;
        try {
            athleteDetails = this.swimRankingBrowserService.getAthleteDetails(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (athleteDetails.isPresent()) {
            Optional<Athlete> foundAthlete = this.athleteRepository.findBySwimRankingId(id);
            Optional<Club> foundClub = this.clubRepository.findByName(athleteDetails.get().club());
            Club club = foundClub.orElseGet(() -> this.clubRepository.save((new Club()).setName(athleteDetails.get().club())));
            logger.info("Club:{}", club);
            Athlete athlete = foundAthlete.orElseGet(() -> this.athleteRepository.save((new Athlete()).setSwimRankingId(id).setName(athleteDetails.get().name()).setClub(club)));
            logger.info("Athlete:{}", athlete);
            if (clearCache) {
                for (String year : years) {
                    this.timeRepository.deleteAllByAthleteIdAndCompetitionDateContains(
                            athlete.getId(), year
                    );
                }
            }
            athleteDetails.get().competitionList().forEach((competition) -> this.importCompetition(athlete, competition, clearCache, years));
        }
    }

    private void importCompetition(Athlete athlete, SwimRankingBrowserService.Competition comp, boolean clearCache, String... years) {
        Optional<Competition> foundCompetition = this.competitionRepository.findBySwimRankingId(comp.swimRankingId());
        if (DateUtil.isYearOfInterest(comp.date(), years)) {
            Competition competition = foundCompetition.orElseGet(() -> {
                try {
                    return this.competitionRepository.save(new Competition()
                            .setCity(comp.city())
                            .setName(this.swimRankingBrowserService.getCompetitionName(comp.swimRankingId(), comp.clubId()))
                            .setDate(comp.date())
                            .setCourse(comp.course())
                            .setSwimRankingId(comp.swimRankingId()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            if (competition.getCity() == null) {
                try {
                    competition.setCity(competition.getName());
                    competition.setName(this.swimRankingBrowserService.getCompetitionName(comp.swimRankingId(), comp.clubId()));
                    this.competitionRepository.save(competition);
                } catch (IOException e) {
                    logger.error("Could not find competition name");
                }
            }
            logger.info("Competition:{}", competition);

            try {
                this.swimRankingBrowserService.getAthleteTimes(athlete.getName(), comp.swimRankingId(), comp.clubId(), clearCache).forEach((time) -> {
                    Optional<Stroke> foundStroke = this.strokeRepository.findByName(time.stroke());
                    Stroke stroke = foundStroke.orElseGet(() -> this.strokeRepository.save((new Stroke()).setName(time.stroke()).setDistance(time.distance())));
                    Optional<Time> foundTime = this.timeRepository.findByAthleteAndStrokeAndCompetition(athlete, stroke, competition);
                    if (foundTime.isEmpty()) {
                        this.timeRepository.save(new Time()
                                .setCompetition(competition)
                                .setStroke(stroke)
                                .setAthlete(athlete)
                                .setTime(time.time())
                                .setSeconds(time.seconds())
                                .setPoints(time.points())
                        );
                    } else {
                        var previousTime = foundTime.get();
                        if (previousTime.getPoints() == null || previousTime.getPoints() < time.points()) {
                            this.timeRepository.save(
                                    previousTime
                                            .setPoints(time.points())
                                            .setTime(time.time())
                            );
                        }
                    }

                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}