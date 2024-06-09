package lu.cnw.ranking.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DistanceConverter {
    public static int computeDistance(String distanceDescription) {
        try {
            var split = distanceDescription.split("x");
            int distanceInMeters = Integer.parseInt(split[split.length - 1].split("m")[0].trim());
            int multiplier = split.length == 1 ? 1 : Integer.parseInt(split[0].trim());
            return distanceInMeters * multiplier;
        } catch (NumberFormatException e) {
            logger.error("Could not parse {}", distanceDescription);
            return 0;
        }
    }
}
