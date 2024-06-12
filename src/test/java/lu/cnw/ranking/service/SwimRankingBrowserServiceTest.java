package lu.cnw.ranking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class SwimRankingBrowserServiceTest {
    SwimRankingBrowserService service;

    @BeforeEach
    void setup() {
        service = new SwimRankingBrowserService();
        service.athleteDetails = "page=athleteDetail&athleteId=%s";
        service.meetDetails = "page=meetDetail&meetId=%s&clubId=%s";
        service.cacheFolder = "./target/cache";
        service.swimRangingUrl = "https://www.swimrankings.net/index.php?";
    }

    @Test
    void getAthleteDetails() throws IOException {
        var details = service.getAthleteDetails("5423869");
        System.out.println(details);
        details.competitionList().forEach(competition -> {
            try {
                System.out.println(service.getAthleteTimes(
                        details.name(),
                        competition.swimRankingId(),
                        competition.clubId()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void getAthlete2() throws IOException {
        service.getAthleteDetails("5478192").competitionList().forEach(
                c -> {
                    try {
                        service.getAthleteTimes("GASPARD, Olivia", c.swimRankingId(), c.clubId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Test
    void getAthleteTimes() throws IOException {
        var times = service.getAthleteTimes("EVEN, Renaud", "645678", "73544");
        System.out.println(times);
    }

    @Test
    void findAthletes() throws IOException {
        var athletes = service.findAthletes("66272");
        System.out.println(athletes);
    }
}