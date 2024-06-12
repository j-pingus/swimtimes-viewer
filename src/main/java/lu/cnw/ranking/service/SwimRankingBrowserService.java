package lu.cnw.ranking.service;

import lombok.extern.slf4j.Slf4j;
import lu.cnw.ranking.utils.DistanceConverter;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class SwimRankingBrowserService {
    @Value("${ranking.cache}")
    String cacheFolder;
    @Value("${ranking.url}")
    String swimRangingUrl;
    @Value("${ranking.athleteDetails}")
    String athleteDetails;
    @Value("${ranking.meetDetails}")
    String meetDetails;

    public AthleteDetails getAthleteDetails(String id) throws IOException {
        String key = "athlete/" + id;
        var detailsHtmlDoc = Jsoup.parse(getCached(Retention.DAY, key, athleteDetails, id));
        var info = detailsHtmlDoc.getElementById("athleteinfo");
        var name = info.getElementById("name");
        var club = info.getElementById("nationclub");
        var competitions = detailsHtmlDoc.select("tr.athleteMeet0,tr.athleteMeet1");
        var competitionList = competitions.stream()
                .map(competition -> {
                    var city = competition.selectFirst("td.city");
                    var link = city.selectFirst("a[href]").attr("href");
                    //https://www.swimrankings.net/index.php?page=meetDetail&meetId=645678&clubId=73544
                    var splitted = link.split("&");
                    var meetId = splitted[1].split("=")[1];
                    var clubId = splitted[2].split("=")[1];
                    return new Competition(
                            meetId,
                            getFirstText(competition, "td.date"),
                            getFirstText(competition, "td.city"),
                            getFirstText(competition, "td.course"),
                            clubId);
                }).toList();
        return new AthleteDetails(
                id,
                name.childNode(0).toString().trim(),
                club.text(),
                competitionList);
    }

    public List<AthleteTime> getAthleteTimes(String name, String meetId, String clubId) throws IOException {
        String key = "competition/" + meetId + "/" + clubId;
        String competitionDetails = getCached(Retention.FOREVER, key, meetDetails, meetId, clubId);
        var parsedDoc = Jsoup.parse(competitionDetails);
        var competitionRows = parsedDoc.select("tr.meetResult1,tr.meetResult0");
        boolean take = false;
        List<AthleteTime> times = new ArrayList<>();
        for (Element row : competitionRows) {
            if (row.hasClass("meetResult1")) {
                take = row.text().contains(name);
                continue;
            }
            if (take) {
                if (row.select("td.name").size() > 1) break;
                String time = getFirstText(row, "a.time");
                String stroke = getFirstText(row, "td.name");
                times.add(new AthleteTime(
                        time,
                        getSeconds(time),
                        stroke,
                        DistanceConverter.computeDistance(stroke),
                        getFirstText(row, "td.meetPlace"))
                );
            }
        }
        return times;
    }

    private Double getSeconds(String time) {
        if (!time.contains(".")) {
            return 0d;
        }
        var splitDec = time.split("\\.");
        var decimals = Integer.valueOf(splitDec[1]).doubleValue() / 100;
        if (splitDec[0].contains(":")) {
            var split = splitDec[0].split(":");
            if (split.length == 2) {
                var seconds = Integer.valueOf(split[1]).doubleValue();
                var minutes = Integer.valueOf(split[0]).doubleValue() * 60;
                return minutes + seconds + decimals;
            } else if (split.length == 3) {
                var seconds = Integer.valueOf(split[2]).doubleValue();
                var minutes = Integer.valueOf(split[1]).doubleValue() * 60;
                var hours = Integer.valueOf(split[1]).doubleValue() * 3600;
                return hours + minutes + seconds + decimals;
            }
            return 9999999d;
        } else {
            return Integer.valueOf(splitDec[0]).doubleValue() + decimals;
        }
    }

    private String getFirstText(Element element, String cssSelector) {
        var found = element.selectFirst(cssSelector);
        return found == null ? "" : found.text();
    }

    public Set<AthleteDetails> findAthletes(String clubId) {
        Set<AthleteDetails> ret = new HashSet<>();
        FileUtils.listFiles(new File(cacheFolder, "competition"), null, true).forEach(
                file -> {
                    if (file.getName().equals(clubId)) {
                        try {
                            Jsoup.parse(file).select("tr.meetResult1").forEach(
                                    row -> {
                                        var anchor = row.selectFirst("a");
                                        var url = anchor.attr("href");
                                        var pos = url.indexOf("athleteId=");
                                        if (pos > -1) {
                                            var athleteId = url.substring(pos + 10);
                                            var name = anchor.text();
                                            ret.add(new AthleteDetails(athleteId, name, null, null));
                                        }
                                    }
                            );
                        } catch (IOException e) {
                            logger.error("Could not process {}", file, e);
                        }
                    }
                }
        );
        return ret;
    }

    private String getCached(Retention retention, String key, String page, String... args) throws IOException {
        File cached = new File(cacheFolder, key);
        if (cached.exists() && retention == Retention.DAY) {
            Path file = Paths.get(cached.getAbsolutePath());
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            long DAY = 24 * 60 * 60 * 1000;
            if (attr.creationTime().toMillis() + DAY < System.currentTimeMillis()) {
                cached.delete();
            }
        }
        if (!cached.exists()) {
            File parent = cached.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            //https://www.swimrankings.net/index.php?page=meetDetail&meetId=645678&clubId=73544
            FileUtils.writeStringToFile(cached,
                    Jsoup.connect(swimRangingUrl + String.format(page, (Object[]) args))
                            .execute().body(),
                    Charset.defaultCharset());
        }
        return FileUtils.readFileToString(cached, Charset.defaultCharset());
    }

    private enum Retention {
        DAY, FOREVER
    }

    public record AthleteTime(String time, Double seconds, String stroke, int distance, String place) {
    }

    public record AthleteDetails(String swimRankingId, String name, String club,
                                 java.util.List<Competition> competitionList) {
    }

    public record Competition(String swimRankingId, String date, String city, String course, String clubId) {
    }
}
