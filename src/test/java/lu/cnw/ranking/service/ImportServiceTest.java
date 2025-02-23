package lu.cnw.ranking.service;

import jakarta.inject.Inject;
import lu.cnw.ranking.repository.AthleteRepository;
import lu.cnw.ranking.repository.CompetitionRepository;
import lu.cnw.ranking.repository.StrokeRepository;
import lu.cnw.ranking.repository.TimeRepository;
import lu.cnw.ranking.utils.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class ImportServiceTest {
    @Inject
    ImportService service;
    @Inject
    AthleteRepository repository;
    @Inject
    CompetitionRepository competitionRepository;
    @Inject
    StrokeRepository strokeRepository;
    @Inject
    TimeRepository timeRepository;

    @Test
    void importAthlete() throws IOException {
        service.importAthlete("5423869", false, DateUtil.getYearsOfInterest());
        service.importAthlete("4972119", false, DateUtil.getYearsOfInterest());
        System.out.println(repository.findAll());
        System.out.println(competitionRepository.findAll());
        System.out.println(strokeRepository.findAll());
        System.out.println(timeRepository.findAll());
    }
}