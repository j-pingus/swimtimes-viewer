package lu.cnw.ranking.utils;

public class DistanceConverter {
  public static int computeDistance(String distanceDescription) {
    var split = distanceDescription.split("x");
    int distanceInMeters = Integer.parseInt(split[split.length - 1].split("m")[0].trim());
    int multiplier = split.length == 1 ? 1 : Integer.parseInt(split[0].trim());
    return distanceInMeters * multiplier;
  }
}
